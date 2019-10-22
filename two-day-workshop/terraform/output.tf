output "workstation_ips" {
  value = "${aws_instance.workstation.*.public_ip}"
}
