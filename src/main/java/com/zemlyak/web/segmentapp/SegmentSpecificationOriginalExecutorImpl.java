package com.zemlyak.web.segmentapp;

import com.zemlyak.web.segmentapp.model.DataProvider_;
import com.zemlyak.web.segmentapp.model.SegmentType_;
import com.zemlyak.web.segmentapp.model2.CountryStat_;
import com.zemlyak.web.segmentapp.model2.Segment;
import com.zemlyak.web.segmentapp.model2.Segment_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.ListAttribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class SegmentSpecificationOriginalExecutorImpl implements SegmentSpecificationOriginalExecutor {
    @PersistenceContext
    private EntityManager em;

    public List<Segment> findAllOriginal(Specification<Segment> spec) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Segment> cq = cb.createQuery(Segment.class);
        Root<Segment> root = cq.from(Segment.class);
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, cq, cb);

            if (predicate != null) {
                cq.where(predicate);
            }
        }
        cq
                .select(cb
                        .construct(
                                Segment.class,
                                root.get(Segment_.id),
                                root.get(Segment_.name),
                                root.get(Segment_.active),
                                root.get(Segment_.dataProvider).get(DataProvider_.id),
                                root.get(Segment_.dataProvider).get(DataProvider_.name),
                                root.get(Segment_.segmentType).get(SegmentType_.id),
                                root.get(Segment_.segmentType).get(SegmentType_.name),
                                root.get(Segment_.segmentType).get(SegmentType_.viewName),
                                join(root, Segment_.countryStats).get(CountryStat_.countryCode),
                                join(root, Segment_.countryStats).get(CountryStat_.activeProfilesAmount),
                                join(root, Segment_.countryStats).get(CountryStat_.sleepingProfilesAmount)
                        ));
        return em.createQuery(cq).getResultList();
    }

    @SuppressWarnings("unchecked")
    public static <J,T> ListJoin<J, T> join(From<?, J> from, ListAttribute<J, T> attribute) {
        return from
                .getJoins()
                .stream()
                .filter(join -> Objects.equals(join.getAttribute(), attribute))
                .map(o -> (ListJoin<J, T>)o)
                .findFirst()
                .orElseGet(() -> from.joinList(attribute.getName(), JoinType.LEFT));
    }
}
