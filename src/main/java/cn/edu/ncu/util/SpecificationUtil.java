package cn.edu.ncu.util;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * The Specification Util
 * @author lorry
 * @author lin864464995@163.com
 */
public class SpecificationUtil {
    private Map<String, Object> equalMap = new HashMap<>();
    private Map<String, Object> likeMap = new HashMap<>();
    private Map<String, Date> greaterDateMap = new HashMap<>();
    private Map<String, Date> lessDateMap = new HashMap<>();

    public void addEqualMap(String key, Object value) {
        equalMap.put(key, value);
    }

    public void addEqualMap(Map<String, Object> map) {
        equalMap.putAll(map);
    }

    public void addLikeMap(String key, Object value) {
        likeMap.put(key, value);
    }

    public void addLikeMap(Map<String, Object> map) {
        likeMap.putAll(map);
    }

    public void addGreaterDateMap(String key, Date date) {
        greaterDateMap.put(key, date);
    }

    public void addGreaterDateMap(Map<String, Date> dateMap) {
        greaterDateMap.putAll(dateMap);
    }

    public void addLessDateMap(String key, Date date) {
        lessDateMap.put(key, date);
    }

    public void addLessDateMap(Map<String, Date> dateMap) {
        greaterDateMap.putAll(dateMap);
    }


    public <E> Specification<E> getSpecification() {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (equalMap.size() != 0) {
                equalMap.forEach((key, value) -> {
                    if (key.length() != 0) {
                        predicates.add(criteriaBuilder.equal(root.get(key), value));
                    }
                });
            }

            if (likeMap.size() != 0) {
                likeMap.forEach((key, value) -> {
                    if (key.length() != 0) {
                        predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                    }
                });
            }

            if (greaterDateMap.size() != 0) {
                greaterDateMap.forEach((key, value) -> {
                    if (key.length() != 0) {
                        predicates.add(criteriaBuilder.greaterThan(root.get(key), value));
                    }
                });
            }

            if (lessDateMap.size() != 0) {
                lessDateMap.forEach((key, value) -> {
                    if (key.length() != 0) {
                        predicates.add(criteriaBuilder.lessThan(root.get(key), value));
                    }
                });
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
