package com.tienda.services.impl;

import com.tienda.dtos.marca.MarcaRequest;
import com.tienda.dtos.marca.MarcaResponse;
import com.tienda.mappers.MarcaMapper;
import com.tienda.models.Marca;
import com.tienda.repositories.MarcaRepository;
import com.tienda.services.IMarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaServiceImpl implements IMarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    @Override
    public List<MarcaResponse> listarTodas() {
        return marcaMapper.toResponseList(marcaRepository.findAll());
    }

    @Override
    public MarcaResponse crear(MarcaRequest request) {
        Marca marca = marcaMapper.toEntity(request);
        return marcaMapper.toResponse(marcaRepository.save(marca));
    }
}
