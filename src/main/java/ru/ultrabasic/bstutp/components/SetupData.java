package ru.ultrabasic.bstutp.components;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ultrabasic.bstutp.models.hierarchy.Competence;
import ru.ultrabasic.bstutp.models.hierarchy.CompetenceGroup;
import ru.ultrabasic.bstutp.models.hierarchy.Level;
import ru.ultrabasic.bstutp.models.users.Admin;
import ru.ultrabasic.bstutp.models.users.Student;
import ru.ultrabasic.bstutp.models.users.Teacher;
import ru.ultrabasic.bstutp.repositories.CompetenceGroupRepository;
import ru.ultrabasic.bstutp.repositories.CompetenceRepository;
import ru.ultrabasic.bstutp.repositories.LevelRepository;
import ru.ultrabasic.bstutp.repositories.UserRepository;

import java.util.Collections;
import java.util.Set;

@Component
public class SetupData {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private CompetenceGroupRepository competenceGroupRepository;
    @Autowired
    private CompetenceRepository competenceRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        initLevels();
        initCompetencesGroups();
        initUsers();
    }

    public void initCompetencesGroups() {
        competenceGroupRepository.save(new CompetenceGroup(null, "Группа",
                Set.of(new Competence(null, 1L, "Классная компетенция", Collections.emptySet(),
                        levelRepository.findById(1L).get()))));
    }

    public void initLevels() {
        levelRepository.save(new Level(null, "Бакалавриат"));
        levelRepository.save(new Level(null, "Магистратура"));
        levelRepository.save(new Level(null, "Специалитет"));
    }

    public void initUsers() {
        userRepository.save(new Admin(null, "admin", passwordEncoder.encode("admin"),
                "Иван", "Иванов", "Иванович"));
        userRepository.save(new Student(null, "student", passwordEncoder.encode("student"),
                "Студент", "Студент", null, 1000000L));
        userRepository.save(new Student(null, "student1", passwordEncoder.encode("student"),
                "Студент1", "Студент", "null", 1000100L));
        userRepository.save(new Teacher(null, "onizuka", passwordEncoder.encode("onizuka"),
                "onizuka", "onizuka", "onizuka"));
    }
}
