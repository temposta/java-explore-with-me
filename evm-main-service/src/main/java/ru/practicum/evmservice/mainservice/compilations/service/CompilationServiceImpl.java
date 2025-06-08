package ru.practicum.evmservice.mainservice.compilations.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.mainservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.evmservice.mainservice.compilations.dto.UpdateCompilationRequest;
import ru.practicum.evmservice.mainservice.compilations.mapper.CompilationMapper;
import ru.practicum.evmservice.mainservice.compilations.model.CompilationFull;
import ru.practicum.evmservice.mainservice.compilations.repository.CompilationEventsRepository;
import ru.practicum.evmservice.mainservice.compilations.repository.FullCompilationRepository;
import ru.practicum.evmservice.mainservice.events.model.Event;
import ru.practicum.evmservice.mainservice.events.repository.EventRepository;
import ru.practicum.evmservice.mainservice.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationEventsRepository compilationEventsRepository;
    private final CompilationMapper compilationMapper;
    private final FullCompilationRepository fullCompilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        CompilationFull compilation = compilationMapper.fromDto(newCompilationDto);
        List<Event> events = new ArrayList<>();
        if (!newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }
        compilation.getEvents().addAll(events);
        fullCompilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }

    @Override
    public void deleteCompilation(Integer compId) {
        fullCompilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%s was not found", compId)));
        fullCompilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilationRequest) {
        CompilationFull compilation = fullCompilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%s was not found", compId)));
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getEvents() != null) {
            compilationEventsRepository.deleteCompilationEventsById(compId);
            List<Event> events = new ArrayList<>();
            if (!updateCompilationRequest.getEvents().isEmpty()) {
                events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            }
            compilation.getEvents().addAll(events);
        }
        compilation = fullCompilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }

    @Override
    public CompilationDto getCompilation(Integer compId) {
        CompilationFull compilation = fullCompilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%s was not found", compId)));
        return compilationMapper.toDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<CompilationFull> compilations = fullCompilationRepository.getCompilationsByPinned(pinned, pageable);
        return compilationMapper.toDtoList(compilations);
    }
}
