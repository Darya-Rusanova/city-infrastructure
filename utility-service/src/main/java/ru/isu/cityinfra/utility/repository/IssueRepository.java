package ru.isu.cityinfra.utility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.cityinfra.utility.enums.IssueStatus;
import ru.isu.cityinfra.utility.model.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue,Integer> {
    Issue findByUserId(Integer userId);
    Issue findByStatus(IssueStatus status);
    List<Issue> findAllByUserId(Integer userId);
}
