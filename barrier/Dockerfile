FROM openjdk:11

# add the build dependencies
RUN echo "Installing dependencies" \
     && apt update  \
     && apt install \
                zip \
    #            linux-cpupower -y \
    && /bin/bash -c 'curl -s "https://get.sdkman.io" | bash' \
    && echo "Finished installing dependencies"

# copy current source code into the container
ADD . /home/barrier
WORKDIR /home/barrier

RUN echo "Building source code" \
    && /bin/bash -c 'source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install gradle && gradle assemble' \
     # remove gradle and all dependencies
     && rm -rf $HOME/.sdkman/candidates/gradle $HOME/.sdkman/archives/gradle  $HOME/.gradle \
     && echo "Code built"

CMD /bin/bash