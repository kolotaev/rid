package com.github.kolotaev.ride

import scala.util.Random
import java.security.MessageDigest
import java.net.{InetAddress, UnknownHostException}


object System {

  private val random = new Random(new java.security.SecureRandom())

  def machineID: Array[Byte] = {
    try
      MessageDigest.getInstance("MD5")
        .digest(InetAddress.getLocalHost.getHostName.getBytes)
        .slice(0, 3)
    catch {
      case _: UnknownHostException =>
        // Fallback to random number if hostname is unavailable
        val bytes = Array.fill[Byte](3)(0)
        random.nextBytes(bytes)
        bytes
    }
  }

  def randomInt: Int = {
    val bytes: Array[Byte] = Array.fill[Byte](3)(0)
    random.nextBytes(bytes)
    bytes(0) << 16 | bytes(1) << 8 | bytes(2)
  }

  def timestamp: Int = (java.lang.System.currentTimeMillis / 1000).toInt

  def processID: Int = {
    // default to random value if PID can't be obtained. In most cases it won't happen
    var result: Int = random.nextInt
    try result = management.ManagementFactory.getRuntimeMXBean.getName.split("@")(0).toInt
    catch { case _: Exception => }
    result
  }
}
