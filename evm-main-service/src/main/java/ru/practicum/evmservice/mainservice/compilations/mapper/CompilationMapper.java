package ru.practicum.evmservice.mainservice.compilations.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.evmservice.mainservice.compilations.dto.CompilationDto;
import ru.practicum.evmservice.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.evmservice.mainservice.compilations.model.CompilationFull;
import ru.practicum.evmservice.mainservice.events.mapper.EventMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompilationMapper {

    public CompilationFull fromDto(NewCompilationDto newCompilationDto) {
        CompilationFull compilation = new CompilationFull();
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        return compilation;
    }

    public CompilationDto toDto(CompilationFull compilation) {
        return CompilationDto.builder()
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .id(compilation.getId())
                .events(EventMapper.INSTANCE.toShortDtos(compilation.getEvents().stream().toList()))
                .build();
    }

    public List<CompilationDto> toDtoList(List<CompilationFull> compilations) {
        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (CompilationFull c : compilations) {
            compilationDtos.add(CompilationDto.builder()
                    .pinned(c.getPinned())
                    .title(c.getTitle())
                    .id(c.getId())
                    .events(EventMapper.INSTANCE.toShortDtos(c.getEvents().stream().toList()))
                    .build());
        }
        return compilationDtos;
    }
}
