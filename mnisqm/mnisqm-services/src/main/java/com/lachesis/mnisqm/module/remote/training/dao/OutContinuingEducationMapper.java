package com.lachesis.mnisqm.module.remote.training.dao;

import com.lachesis.mnisqm.module.remote.training.domain.ContinuingEducation;
import java.util.List;

public interface OutContinuingEducationMapper {
    int insert(ContinuingEducation record);

    List<ContinuingEducation> selectAll();
}