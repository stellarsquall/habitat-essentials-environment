# Habitat Workshop
This repository contains files used for the Habitat Essentials 2-day training.

## Work to do

### Move Workshop Sample Node App to Chef Repository
Currently [Sample Node App](https://github.com/Indellient/sample-node-app), originally cloned from [habitat-sh](https://github.com/habitat-sh/sample-node-app), is added as a submodule of this repository. This needs to be moved to a chef-specific repository and the graphics in the slides (specifically for the CI example) need to be updated accordingly.

Note that the `workshop` branch should remain the default for cloning as it's a clean version of the repo - no Habitat plan. The master retains the plan (modified from the clone source for bindings) for the sake of the Jenkins example.

### Update Images
Many of the images were taken from the Chef Habitat website source files. Some of these images were modified to remove specific imagery that is no longer allowed to be used, but these should be updated come new imagery. The new logo is used in a number of places, replacing the old logo, and the slides have been created using the newest Chef Learning template available.

### Move Gists
A number of Github Gists are used within the workshop - not sure what the Chef Learning Team prefers to use. These can either be pasted into text files, or any other site, though Github Gists are convenient with the single drawback that they must be associated with a user account, and cannot be associated with an organization (hence `gist.github.com/sirajrauff/blah`). 

## Virtual Machines
### Packer Build
The [Packer build](packer/aws-hvm-workshop-habitat-lnx.json) is based on a [CentOS 7](https://aws.amazon.com/marketplace/pp/B00O7WM7QW?qid=1564064885779&sr=0-1&ref_=srh_res_product_title) image from Centos.org with the following modifications:

- Tree, Git (2.0+) and Docker are installed
- Docker Daemon is enabled
- `chef` user created with password `Cod3Can!`
    - User is added to groups `wheel`, `root`, and `docker`
    - `NOPASSWD` enabled for `sudo`
    - **/etc/ssh/sshd_config** is modified to accept password authentication for SSH, as is **/etc/cloud/cloud.cfg** (this will reset the value in **/etc/ssh/sshd_config** if not modified)
    
### Provision Time
Note when provisioning, regardless of with Vagrant or Terraform the following cocurs:

- [Visual Studio Code Server](https://github.com/cdr/code-server) installed and enabled as a service, running as user `chef`, by default opened to the user's home directory **/home/hab**
  - The addition of this allows us to use a Terminal inside the Visual Studio Code Editor - this ensures less context/window switching and is encouraged to be used.
  - The settings file is modified to ensure line-endings are always `LF`
  - For the two day workshop the settings are modified on machine #2 to enable the light theme, to be able to easily tell which machine you're using.

#### Terraform
There is terraform meant to spin up the AMI given by the Packer build, currently written using Terraform ~0.11.0. Note the security groups here, as if the examples are modified to use different ports, those ports will need to be added/removed here.

Workstations are provisioned with code-server installed and running on port 8443

https://github.com/cdr/code-server

Install terraform plugins with `terraform init`, configure your terraform.tfvars as shown by the terraform.tfvars.example file, specifying the workstation_count. Passwords for the `chef` user are set with workstation_user_password, and to log into the web UI for VSCode set code_server_password. Visit the IP produced by the outputs on port 8443, ie:

http://123.45.67.890:8443

Proceed past the security risk dialogue, and enter the code_server_password. Students can also complete the exercises with pure SSH if preferred.

Each student will have two instances provisioned for them, one should be assigned as their worksatation, the other will be configured as an HAProxy server. All instances are identical and will have code-server available, learners should simply use one as their workstation and another for haproxy.
    
#### Vagrant
A Vagrantfile is created for each workshop, making use of the same scripts used to provision the AMI with the exception of the cloud-specific modification of **/etc/ssh/sshd_config**, ensuring a consistent testing environment. Some notable bits:

- When connecting to the machine using `vagrant ssh machine<#>`, you will be connected as the `vagrant` user. Switch to the `hab` user using `su hab` followed by `cd ~`.
- The **\<workshop\>/lab-cheat-sheets** folder is mounted at **/lab-cheat-sheets**
  - Possibly add this to the terraform as well eventually? Not sure how we want to distribute the cheat-sheets.
- The Habitat Supervisor will resolve the system IP to the first network interface that is able to access Google's DNS over UDP. This means that with the default Vagrant configuration of having the NAT interface as the first network interface, peering will not work, hence the strange hacks in the two-day workshop Vagrantfile to swap the network adapters after the machine comes up
- The two-day workshop makes use of a public network (bridged-adapter) and the name of such adapter may need to be changed for the machine it is run on. Note that this is sometimes finicky and will lose connection temporarily (usaully no more than 30 seconds)
- When running the two day workshop, Jenkins listens on `{{ sys.ip }}` which results in it listening to the bridged network adapter and *not* the NAT adapter; hence, you can't access it using the forwarded port on `localhost:80`, and you must access it on the public network IP.

## Jenkins
A bespoke [Jenkins package](two-day-workshop/jenkins) was created for the sake of this workshop with the following modifications:

- Creates a stub credential called `hab-depot-credential`
- Updated to latest LTS
- Disables first-run prompts
- Installs a few plugins serially when the package is starting up, including BlueOcean, Git, GitHub, etc. Note this increases start up time but ensures all plugins are available in the other configuration templates (hence being named **a-plugins** as this script must be run first.
- Password and username set to `admin`/`admin`
