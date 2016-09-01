package com.lachesis.mnisqm.module.remote.training.dao;

import com.lachesis.mnisqm.module.training.domain.NurseLevelTraining;
import java.util.List;

public interface NurseLevelTrainingMapper {
    int insert(NurseLevelTraining record);

    List<NurseLevelTraining> selectAll();
}