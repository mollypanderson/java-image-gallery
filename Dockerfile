FROM ubuntu:latest

ENV PG_HOST=image-gallery.cfunveg3cqlp.us-west-2.rds.amazonaws.com
ENV PG_PORT=5432
ENV IG_DATABASE=image_gallery
ENV IG_USER=image_gallery
ENV IG_PASSWD=Jas33per

RUN apt-get update -y && apt install default-jre -y
RUN apt-get install -y git
RUN git clone https://github.com/mollypanderson/java-image-gallery.git

COPY java-image-gallery/build/libs/java-image-gallery-all.jar /java-image-gallery/
WORKDIR /java-image-gallery

CMD ["java","-jar","java-image-gallery-all.jar"]
