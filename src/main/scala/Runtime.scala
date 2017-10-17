import com.monsanto.arch.cloudformation.model._
import com.monsanto.arch.cloudformation.model.resource.S3Event.`s3:ObjectCreated:*`
import com.monsanto.arch.cloudformation.model.resource._
import com.monsanto.arch.cloudformation.model.simple.Builders._

/**
  * Created by sturmm on 16.10.17.
  */
object RuntimeLayer extends VPCWriter with App {

  val script =
    """
      var response = require('cfn-response');
      exports.handler = function(event, context) {
        var input = parseInt(event.ResourceProperties.Input);
        var responseData = {Value: input * 5};
        response.send(event, context, response.SUCCESS, responseData);
      };
    """.stripMargin

  val PREFIX = "jus_a_test"

  val lambda = `AWS::Lambda::Function`(
    name = s"${PREFIX}-funtion",
    Code = Code(
      ZipFile = Option(script),
      S3Key = Option.empty,
      S3ObjectVersion  = Option.empty,
      S3Bucket = Option.empty
    ),
    Description = Option(StringToken("A Description")),
    Handler = "function.lambda_handler",
    Runtime = new Runtime("nodejs6.10"),
    Role = StringToken("MyRole")
  )

  val notification = NotificationConfiguration(
    LambdaConfigurations = Seq(LambdaConfiguration(
      Event = `s3:ObjectCreated:*`,
      Function = lambda.arn
    ))
  )

  val bucket = `AWS::S3::Bucket`(
    name = s"${PREFIX}-adasd-",
    BucketName = Option.empty,
    NotificationConfiguration = Option(notification)
  )


  val template = Template.fromResource(lambda) ++ Template.fromResource(bucket)

  writeStaxModule("vpc-simple.json", template)

}