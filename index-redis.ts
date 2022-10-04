// similar like https://github.com/pulumi/examples/blob/master/aws-ts-voting-app/index.ts

import * as awsx from '@pulumi/awsx';
import * as pulumi from '@pulumi/pulumi';

const config = new pulumi.Config();
const redisPassword = config.require('redisPassword');
const redisPort = 6379;

const redisListener = new awsx.elasticloadbalancingv2.NetworkListener(
  'eu-captcha-cache',
  { port: redisPort }
);
const redisCache = new awsx.ecs.FargateService('eu-captcha-cache', {
  taskDefinitionArgs: {
    containers: {
      redis: {
        image: 'redis:alpine',
        memory: 512,
        portMappings: [redisListener],
        command: ['redis-server', '--requirepass', redisPassword],
      },
    },
  },
  assignPublicIp: true,
});

const redisEndpoint = redisListener.endpoint;

const euCaptchaListener = new awsx.elasticloadbalancingv2.ApplicationListener(
  'eu-captcha-frontend',
  { port: 80 }
);

const euCaptcha = new awsx.ecs.FargateService('eu-captcha-frontend', {
  taskDefinitionArgs: {
    containers: {
      euCaptchaFrontend: {
        image: awsx.ecs.Image.fromPath('eu-captcha-frontend', './'),
        memory: 512,
        portMappings: [euCaptchaListener],
        environment: redisEndpoint.apply((e) => [
          { name: 'REDIS', value: e.hostname },
          { name: 'REDIS_PORT', value: e.port.toString() },
          { name: 'REDIS_PWD', value: redisPassword },
        ]),
      },
    },
  },
  assignPublicIp: true,
});

export let euCaptchaURL = euCaptchaListener.endpoint;
