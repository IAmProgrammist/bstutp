package ru.ultrabasic.bstutp.data.models.records;

import java.util.ArrayList;

public record EducationalProgram(Integer id, String name, ArrayList<Integer> idCompetences) { }
