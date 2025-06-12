package ru.practicum.evmservice.mainservice.requests.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.mainservice.events.dto.EventState;
import ru.practicum.evmservice.mainservice.events.model.Event;
import ru.practicum.evmservice.mainservice.events.repository.EventRepository;
import ru.practicum.evmservice.mainservice.exceptions.ForbiddenException;
import ru.practicum.evmservice.mainservice.exceptions.IncorrectRequestException;
import ru.practicum.evmservice.mainservice.exceptions.NotFoundException;
import ru.practicum.evmservice.mainservice.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.evmservice.mainservice.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.evmservice.mainservice.requests.dto.ParticipationRequestDto;
import ru.practicum.evmservice.mainservice.requests.dto.RequestStatus;
import ru.practicum.evmservice.mainservice.requests.mapper.RequestMapper;
import ru.practicum.evmservice.mainservice.requests.model.Request;
import ru.practicum.evmservice.mainservice.requests.repository.RequestRepository;
import ru.practicum.evmservice.mainservice.users.model.User;
import ru.practicum.evmservice.mainservice.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(int userId) {
        User user = checkUser(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(user.getId());
        return RequestMapper.INSTANCE.toParticipationDtoList(requests);
    }

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        Optional<Request> requestOptional = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (requestOptional.isPresent()
            && (RequestStatus.PENDING.equals(requestOptional.get().getStatus())
                || requestOptional.get().getStatus().equals(RequestStatus.CONFIRMED))) {
            throw new ForbiddenException("You are not allowed to create a request: request already exists");
        }
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new IncorrectRequestException("You cannot create request for a current user with id=" + userId);
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new IncorrectRequestException("You cannot create request for a not published event");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new IncorrectRequestException("You cannot create request for a participant limit exceeded");
        }
        Request request = new Request();
        request.setRequesterId(userId);
        request.setEventId(eventId);
        if (event.getRequestModeration() == false || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            eventRepository.incrementConfirmedRequests(eventId, 1);
        }
        request = requestRepository.save(request);
        return RequestMapper.INSTANCE.toParticipationDto(request);
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        checkUser(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request with id=" + requestId + " was not found")
        );
        if (request.getRequesterId() != userId) {
            throw new IncorrectRequestException("You cannot cancel request for a current user with id=" + userId);
        }
        if (request.getStatus() == RequestStatus.CONFIRMED) {
            request.setStatus(RequestStatus.CANCELED);
            requestRepository.save(request);
            eventRepository.decrementConfirmedRequests(request.getEventId());
        } else if (request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.CANCELED);
            requestRepository.save(request);
        }
        return RequestMapper.INSTANCE.toParticipationDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOnEvent(int userId, Integer eventId) {
        checkUser(userId);
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return RequestMapper.INSTANCE.toParticipationDtoList(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(int userId, Integer eventId,
                                                         EventRequestStatusUpdateRequest
                                                                 eventRequestStatusUpdateRequest) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        if (event.getParticipantLimit() == 0 || event.getRequestModeration() == false) {
            throw new IncorrectRequestException("You cannot update requests for a current user with id=" + userId);
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new IncorrectRequestException("You cannot update requests out of limit" +
                                                " for a current user with id=" + userId);
        }

        int limit = event.getParticipantLimit() - event.getConfirmedRequests();
        int counter = 0;
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(new ArrayList<>());
        result.setRejectedRequests(new ArrayList<>());

        List<Request> requests;
        if (eventRequestStatusUpdateRequest.getRequestIds() == null) {
            requests = requestRepository.findAllByRequesterIdAndEventId(userId, eventId);
        } else {
            requests = requestRepository
                    .findAllByEventIdAndIdInOrderByCreatedDateAsc(eventId, eventRequestStatusUpdateRequest.getRequestIds());
        }
        EventRequestStatusUpdateRequest.Status toStatus = eventRequestStatusUpdateRequest.getStatus();


        for (Request request : requests) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new IncorrectRequestException(String
                        .format("You cannot update requests with status=%s for a current user with id=%s",
                                request.getStatus(), userId));
            }
            if (toStatus.equals(EventRequestStatusUpdateRequest.Status.CONFIRMED)) {
                if (counter == limit) {
                    request.setStatus(RequestStatus.REJECTED);
                    result.getRejectedRequests().add(RequestMapper.INSTANCE.toParticipationDto(request));
                } else {
                    request.setStatus(RequestStatus.CONFIRMED);
                    result.getConfirmedRequests().add(RequestMapper.INSTANCE.toParticipationDto(request));
                    counter++;
                }
            } else if (toStatus.equals(EventRequestStatusUpdateRequest.Status.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
                result.getRejectedRequests().add(RequestMapper.INSTANCE.toParticipationDto(request));
            }
        }
        requestRepository.saveAll(requests);
        eventRepository.incrementConfirmedRequests(eventId, counter);
        return result;
    }


    private User checkUser(int userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found")
        );
    }

    private Event checkEvent(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        );
    }
}
