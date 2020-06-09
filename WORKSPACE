#
# GRAKN.AI - THE KNOWLEDGE GRAPH
# Copyright (C) 2020 Grakn Labs Ltd
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

workspace(name = "simulation")


################################
# Load Grakn Labs dependencies #
################################

load("//dependencies/graknlabs:dependencies.bzl", "graknlabs_grakn_core", "graknlabs_client_java", "graknlabs_build_tools")
graknlabs_grakn_core()
graknlabs_client_java()
graknlabs_build_tools()

load("@graknlabs_grakn_core//dependencies/graknlabs:dependencies.bzl", "graknlabs_graql", "graknlabs_protocol")
graknlabs_graql()
graknlabs_protocol()

load("@graknlabs_build_tools//distribution:dependencies.bzl", "graknlabs_bazel_distribution")
graknlabs_bazel_distribution()

load("@graknlabs_build_tools//unused_deps:dependencies.bzl", "unused_deps_dependencies")
unused_deps_dependencies()


###########################
# Load Bazel Dependencies #
###########################

load("@graknlabs_build_tools//bazel:dependencies.bzl", "bazel_common", "bazel_deps", "bazel_toolchain")
bazel_common()
bazel_deps()
bazel_toolchain()


#################################
# Load Build Tools dependencies #
#################################

load("@graknlabs_build_tools//bazel:dependencies.bzl", "bazel_rules_python")
bazel_rules_python()

load("@io_bazel_rules_python//python:pip.bzl", "pip_repositories", "pip_import")
pip_repositories()

pip_import(
    name = "graknlabs_build_tools_ci_pip",
    requirements = "@graknlabs_build_tools//ci:requirements.txt",
)

load("@graknlabs_build_tools_ci_pip//:requirements.bzl",
graknlabs_build_tools_ci_pip_install = "pip_install")
graknlabs_build_tools_ci_pip_install()


###########################
# Load Local Dependencies #
###########################

load("//dependencies/maven:dependencies.bzl", "maven_dependencies")
maven_dependencies()


#######################################
# Load compiler dependencies for GRPC #
#######################################

load("@graknlabs_build_tools//grpc:dependencies.bzl", "grpc_dependencies")
grpc_dependencies()

load("@com_github_grpc_grpc//bazel:grpc_deps.bzl",
com_github_grpc_grpc_deps = "grpc_deps")
com_github_grpc_grpc_deps()

load("@stackb_rules_proto//java:deps.bzl", "java_grpc_compile")
java_grpc_compile()


################################
# Load Grakn Core Dependencies #
################################

load("@graknlabs_grakn_core//dependencies/graknlabs:dependencies.bzl",
"graknlabs_common", "graknlabs_console")
graknlabs_common()
graknlabs_console()

load("@graknlabs_grakn_core//dependencies/maven:dependencies.bzl",
graknlabs_grakn_core_maven_dependencies = "maven_dependencies")
graknlabs_grakn_core_maven_dependencies()

load("@graknlabs_build_tools//bazel:dependencies.bzl", "bazel_rules_docker")
bazel_rules_docker()

load("@graknlabs_bazel_distribution//common:dependencies.bzl", "bazelbuild_rules_pkg")
bazelbuild_rules_pkg()


###########################
# Load Graql Dependencies #
###########################

# for Bazel
load("@graknlabs_graql//dependencies/compilers:dependencies.bzl", "antlr_dependencies")
antlr_dependencies()

# for ANTLR programs
load("@rules_antlr//antlr:deps.bzl", "antlr_dependencies")
antlr_dependencies()

load("@graknlabs_graql//dependencies/maven:dependencies.bzl",
graknlabs_graql_maven_dependencies = "maven_dependencies")
graknlabs_graql_maven_dependencies()

######################
# Load Grabl Tracing #
######################

load("//dependencies/graknlabs:dependencies.bzl", "graknlabs_grabl_tracing")
graknlabs_grabl_tracing()