package ru.isu.cityinfra.utility.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import ru.isu.cityinfra.utility.enums.IssueStatus;
import ru.isu.cityinfra.utility.model.Issue;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Integer>, JpaSpecificationExecutor<Issue> {
    Issue findByUserId(Integer userId);
    Issue findByStatus(IssueStatus status);
}
