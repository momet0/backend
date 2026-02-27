package com.tienda.services;

import com.tienda.dtos.marca.MarcaRequest;
import com.tienda.dtos.marca.MarcaResponse;

import java.util.List;

public interface IMarcaService {
    List<MarcaResponse> listarTodas();
    MarcaResponse crear(MarcaRequest request);
}
