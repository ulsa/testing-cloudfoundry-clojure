# testing-cloudfoundry-clojure

This is a test project written in Clojure. Its purpose is to test deployment on
CloudFoundry, either local (BOSH Lite) or public cloud (run.pivotal.io).

## Installing BOSH Lite

A guide about installing BOSH Lite is [available here](https://blog.starkandwayne.com/2014/12/16/running-cloud-foundry-locally-with-bosh-lite/).
A more detailed guide is [available here](https://blog.starkandwayne.com/2015/10/16/deploying-cloud-foundry-locally-with-bosh-lite-with-mac-osx-late-2015/).

## Switching api between Pivotal and BOSH Lite

```
$ cf api
API endpoint: https://api.run.pivotal.io (API version: 2.44.0)

$ cf api --skip-ssl-validation api.bosh-lite.com
$ cf login
Email> admin
Password> admin
$ cf create-org ulsa-local
$ cf create-space development
$ cf target -o "ulsa-local" -s "development"
```

## Blue/Green deployment

Push app to prod as Blue:

```
$ cf push Blue -b https://github.com/heroku/heroku-buildpack-clojure.git -n testing-cloudfoundry
```

* [Pivotal](https://testing-cloudfoundry.cfapps.io)
* [Local](https://testing-cloudfoundry.bosh-lite.com)

Push new version to temp as Green:

```
$ cf push Green -b https://github.com/heroku/heroku-buildpack-clojure.git -n testing-cloudfoundry-temp
```

Test Green at:

* [Pivotal](https://testing-cloudfoundry-temp.cfapps.io)
* [Local](https://testing-cloudfoundry-temp.bosh-lite.com)

Map Green to same route as Blue:

```
$ cf map-route Green cfapps.io -n testing-cloudfoundry
$ cf map-route Green bosh-lite.com -n testing-cloudfoundry
```

Unmap Blue from prod route:

```
$ cf unmap-route Blue cfapps.io -n testing-cloudfoundry
$ cf unmap-route Blue bosh-lite.com -n testing-cloudfoundry
```

Unmap Green from temp route:

```
$ cf unmap-route Green cfapps.io -n testing-cloudfoundry-temp
$ cf unmap-route Green bosh-lite.com -n testing-cloudfoundry-temp
```

Delete orphaned routes:

```
$ cf routes
space         host                        domain          apps   
development   testing-cloudfoundry        bosh-lite.com   Green   
development   testing-cloudfoundry-temp   bosh-lite.com      

$ cf delete-orphaned-routes -f

$ cf routes
space         host                   domain          apps   
development   testing-cloudfoundry   bosh-lite.com   Green
```

## Bosh Lite

Install [spruce](https://github.com/geofffranks/spruce/releases) (replacement for spiff).

Run a Cloud Foundry service broker for PostgreSQL:

```
$ bosh upload release https://bosh.io/d/github.com/cf-platform-eng/docker-boshrelease
$ bosh upload release https://bosh.io/d/github.com/cloudfoundry-community/postgresql-docker-boshrelease
$ cd ~/Source/Git/cloudfoundry-community
$ git clone https://github.com/cloudfoundry-community/postgresql-docker-boshrelease.git
$ cd postgresql-docker-boshrelease
$ templates/make_manifest warden broker embedded
$ bosh -n deploy
$ bosh vms postgresql-docker-warden-broker-embedded
+------------------------+---------+---------------+-------------+
| Job/index              | State   | Resource Pool | IPs         |
+------------------------+---------+---------------+-------------+
| postgresql_docker_z1/0 | running | small_z1      | 10.244.20.6 |
+------------------------+---------+---------------+-------------+
$ cf create-service-broker postgresql-docker containers containers http://10.244.20.6
$ cf enable-service-access postgresql94
$ cf marketplace
service        plans   description   
postgresql94   free    postgresql 9.4 service for application development and testing
$ cd ~/Source/Git/testing-cloudfoundry-clojure
$ cf create-service postgresql94 free postgresql
$ cf bind-service Blue postgresql
$ cf restage Blue
```

Deploy community services (doesn't work, gateways fail):

```
$ cd ~/Source/Git/cloudfoundry-community
$ git clone https://github.com/cloudfoundry-community/cf-services-contrib-release.git
$ cd cf-services-contrib-release
$ templates/make_manifest warden
$ bosh -n deploy
$ cf create-service-auth-token postgresql core c1oudc0w
```

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2015 Ulrik Sandberg
