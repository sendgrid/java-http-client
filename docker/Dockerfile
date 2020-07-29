FROM store/oracle/serverjre:8

ENV OAI_SPEC_URL="https://raw.githubusercontent.com/sendgrid/sendgrid-oai/master/oai_stoplight.json"

RUN yum install -y git 

WORKDIR /root

# install Prism
ADD https://raw.githubusercontent.com/stoplightio/prism/master/install.sh install.sh
RUN chmod +x ./install.sh && sync && \
    ./install.sh && \
    rm ./install.sh

# set up default sendgrid env
WORKDIR /root/sources
RUN git clone https://github.com/sendgrid/java-http-client.git

WORKDIR /root
RUN ln -s /root/sources/java-http-client/sendgrid

COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh

# Set entrypoint
ENTRYPOINT ["./entrypoint.sh"]
CMD ["--mock"]