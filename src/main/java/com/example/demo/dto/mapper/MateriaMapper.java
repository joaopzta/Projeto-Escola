package com.example.demo.dto.mapper;

import com.example.demo.dto.MateriaDTO;
import com.example.demo.model.Materia;

public class MateriaMapper {

    public static Materia toMateria(MateriaDTO materiaDTO) {
        Materia materia = new Materia();
        materia.setId(materiaDTO.getId());
        materia.setDescricao(materiaDTO.getDescricao());

        return materia;
    }

    public static MateriaDTO toMateriaDTO(Materia materia) {
        return new MateriaDTO(materia.getId(), materia.getDescricao());
    }

}
