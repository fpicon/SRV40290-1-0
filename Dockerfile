FROM maven.wom.cl:5000/fuse7/fuse-java-openshift-appdynamics:4.4.3.23530

LABEL maintainer="WOM" \
    author="VSTS"

USER jboss

ADD target/*.jar /deployments/

USER 185