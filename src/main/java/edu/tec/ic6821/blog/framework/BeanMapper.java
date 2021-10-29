package edu.tec.ic6821.blog.framework;

public interface BeanMapper<From, To> {
    To map(From from);
}
