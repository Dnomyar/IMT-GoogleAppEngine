# appengine-java-quickstart
## Project from CloudSDK official documentation

The code source is copied on purpose from https://github.com/GoogleCloudPlatform/getting-started-java/

See this [specific folder](https://github.com/GoogleCloudPlatform/getting-started-java/tree/master/appengine-standard-java8/helloworld) to check the source code


## Usage

### Local test 
```$xslt
docker run --rm -it -h localhost -v ~/.m2:/root/.m2 -v $(pwd):/usr/src/app -v ~/.config/gcloud:/root/.config/gcloud -w /usr/src/app -p 8080:8080 zenika/alpine-appengine-java
```


### Deploy
```$xslt
docker run --rm -it -h localhost -v ~/.m2:/root/.m2 -v $(pwd):/usr/src/app -v ~/.config/gcloud:/root/.config/gcloud -w /usr/src/app -p 8080:8080 zenika/alpine-appengine-java mvn -Dapp.deploy.version=<insert_version_here> appengine:deploy
```


### Deploy indexes
```$xslt
docker run --rm -it -h localhost -v ~/.m2:/root/.m2 -v $(pwd):/usr/src/app -v ~/.config/gcloud:/root/.config/gcloud -w /usr/src/app -p 8080:8080 zenika/alpine-appengine-java mvn -Dapp.deploy.version=<insert_version_here> appengine:deployIndex
```
