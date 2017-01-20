import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.CircuitBreaker
import com.redelastic.example.Metrics
import smoke.{Server, _}
import com.typesafe.config.ConfigFactory

object MetricsExampleApp extends App {
  val smoke = new BasicExampleSmoke
  val prometheusServlet = new PrometheusClientServlet
}

class BasicExampleSmoke extends Smoke  {
  val smokeConfig = ConfigFactory.load().getConfig("smoke")
  val system = ActorSystem()
  implicit val executionContext = system.dispatcher

  onRequest {
//    case GET(Path("/timer")) ⇒
//      Metrics.ExampleDomain.withTimeMetric{ => reply {
//        Thread.sleep(1000)
//        Metrics.ExampleDomain.reportSuccess
//        Response(Ok, body = "It took me a second to build this response.\n")
//      }}
    case GET(Path("/increment-gauge")) ⇒ reply {
      Metrics.ExampleDomain.incrementGauge
      Response(Ok, body = "Incremented gauge.\n")
    }
    case GET(Path("/decrement-gauge")) ⇒ reply {
      Metrics.ExampleDomain.decrementGauge
      Response(Ok, body = "Decremented gauge.\n")
    }
    case _ ⇒
      Metrics.ExampleDomain.report404
      reply(Response(NotFound))
  }

  def breakerExample = {
    import scala.concurrent.duration._

    val breaker =
      new CircuitBreaker(
        system.scheduler,
        maxFailures = 5,
        callTimeout = 10 seconds,
        resetTimeout = 1 minute).onOpen(Metrics.ExampleDomain.breakerOpen)
  }
}

class PrometheusClientServlet {
  import io.prometheus.client.exporter.MetricsServlet;
  import org.eclipse.jetty.server.Server;
  import org.eclipse.jetty.servlet.ServletContextHandler;
  import org.eclipse.jetty.servlet.ServletHolder;

  val server = new Server(1234)
  val context = new ServletContextHandler()
  context.setContextPath("/")
  server.setHandler(context)
  context.addServlet(new ServletHolder(
    new MetricsServlet()), "/metrics")
  server.start();
  server.join();
}