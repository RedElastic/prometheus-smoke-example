package com.redelastic.example

import java.util.Date

import io.prometheus.client._
import com.typesafe.config.ConfigFactory
import io.prometheus.client.CollectorRegistry

/**
  * This applicaiton shows instrumentation of a simple REST server:
  * 1) Metrics w/ promethius client
  */

object Metrics {
  val registry = new CollectorRegistry()
  val counter404 = Counter.build().name("errors").help("help").register()
  val counter200 = Counter.build().name("success").help("help").register()
  val counter200 = Counter.build().name("success").labelNames("cart").help("help").register()
  val counterBreakerOpen = Counter.build().name("breakerOpen").help("breaker opened").register()
  val gauge = Gauge.build().name("gauge").help("help").register()

  //  val gaugeWithLabels = Gauge.build().name("labels").help("help").labelNames("l").register(registry);


  object ExampleDomain {
    def report404: Unit = {
      counter404.inc()
    }

    def reportSuccess: Unit = {
      counter200.inc()
    }

    def incrementGauge: Unit = {
      gauge.inc()
    }

    def decrementGauge: Unit = {
      gauge.dec()
    }

    def withTimeMetric[T](f: () => T) = {
//      val startTime = (new Date).getTime
      val res = f
//      val timeTaken = (new Date).getTime - startTime
//
//      metricsClient.recordExecutionTime("response-time", timeTaken)

      res
    }

    def breakerOpen: Unit = {
      counterBreakerOpen.inc()
    }
  }
}
