// similar like https://github.com/pulumi/examples/blob/master/aws-ts-voting-app/index.ts

import * as awsx from '@pulumi/awsx';
import * as pulumi from '@pulumi/pulumi';

const config = new pulumi.Config();
const redisPassword = config.require('redisPassword');
const redisPort = 6379;

const euCaptcha = new awsx.ecs.FargateService('eu-captcha-frontend', {
  taskDefinitionArgs: {
    containers: {
      euCaptchaFrontend: {
        image: awsx.ecs.Image.fromPath('eu-captcha-frontend', './'),
        memory: 1024,
        cpu: 512,
      },
    },
  },
  assignPublicIp: true,
  desiredCount: 1,
});
