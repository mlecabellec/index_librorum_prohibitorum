#!/bin/bash

mvn clean dependency:go-offline checkstyle:checkstyle pmd:pmd license:aggregate-add-third-party license:aggregate-third-party-report compile package
