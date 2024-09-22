package com.example.jersey;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {
    @XmlAttribute(name = "httpmethod")
    private String httpMethod;

    @XmlAttribute(name = "uri")
    private String uri;

    @XmlAttribute(name = "fetchType")
    private String fetchType;

    @XmlAttribute(name = "reportname")
    private String reportName;

    @XmlAttribute(name = "CVName")
    private String cvName;

    @XmlAttribute(name = "countquery")
    private String countQuery;

    @XmlAttribute(name = "primarykey")
    private String primaryKey;

    @XmlAttribute(name = "entityType")
    private String entityType;

    @XmlElement(name = "Parameter")
    private List<Parameter> parameters;

    // Getters and setters

    @Override
    public String toString() {
        return "Request{" +
                "httpMethod='" + httpMethod + '\'' +
                ", uri='" + uri + '\'' +
                ", fetchType='" + fetchType + '\'' +
                ", reportName='" + reportName + '\'' +
                ", cvName='" + cvName + '\'' +
                ", countQuery='" + countQuery + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", entityType='" + entityType + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}

class Parameter {
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "source")
    private String source;

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "value")
    private String value;

    // Getters and setters

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
