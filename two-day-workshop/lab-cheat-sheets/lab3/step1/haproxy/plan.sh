pkg_name=haproxy
pkg_origin=sirajrauff-public
pkg_version="0.1.0"
pkg_maintainer="The Habitat Maintainers <humans@habitat.sh>"
pkg_license=("Apache-2.0")
pkg_deps=(core/haproxy)

pkg_svc_user="root"
pkg_svc_group="root"

pkg_svc_run="haproxy -f $pkg_svc_config_path/haproxy.conf -db"

do_build() {
  return 0
}

do_install() {
  return 0 
}
