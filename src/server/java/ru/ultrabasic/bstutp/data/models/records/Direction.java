package ru.ultrabasic.bstutp.data.models.records;

import ru.ultrabasic.bstutp.data.models.LevelTypes;

public record Direction(Integer id, String espfCode, String espfName, String code, String name, LevelTypes levelType,
                        int idEducationalProgram) {}
