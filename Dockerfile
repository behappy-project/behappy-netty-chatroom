FROM maven:3.8.5-openjdk-17 as build
WORKDIR /user/src/app
COPY . .
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && curl -fsSL -o /user/src/app/node-v12.22.0-linux-x64.tar.gz  https://npm.taobao.org/mirrors/node/v12.22.0/node-v12.22.0-linux-x64.tar.gz \
    && tar -xvf /user/src/app/node-v12.22.0-linux-x64.tar.gz \
    && ln -sf /user/src/app/node-v12.22.0-linux-x64/bin/node /usr/local/bin/node \
    && ln -sf /user/src/app/node-v12.22.0-linux-x64/bin/npm /usr/local/bin/npm \
    && cd client \
    && npm i --registry=https://registry.npm.taobao.org/ \
    && npm run build \
    && cd ../ \
    && mvn clean package -DskipTests
FROM nginx:latest as runtime
WORKDIR /user/src/app
RUN apt update -y \
    && apt install -y openjdk-17-jdk \
    && ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
COPY default.conf /etc/nginx/conf.d/default.conf
ARG JAR_FILE=/user/src/app/target/*.jar
ENV JAVA_OPTS="-Xms128m -Xmx256m -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom" \
    NODE_ENV="production"
COPY --from=build ${JAR_FILE} app.jar
COPY --from=build /user/src/app/client/dist dist
CMD service nginx start && java ${JAVA_OPTS} -jar app.jar
