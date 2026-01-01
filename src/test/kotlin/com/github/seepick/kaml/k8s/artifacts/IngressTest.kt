package com.github.seepick.kaml.k8s.artifacts

/*
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: / # rewrite path, otherwise our "/foo" will be passed to the backend
    nginx.ingress.kubernetes.io/ssl-redirect: "false"
spec:
  rules:
  - http:
      paths:
      - path: /foo
        pathType: Prefix
        backend:
          service:
           name: foo-service
           port:
            number: 8080
 */