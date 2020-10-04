package demo

import chisel3._
import chisel3.util._
import orbital._

class Simple() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(2.W))
  })

  io.out := io.in + 1.U
}

class Demo() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(2.W))
    val valid = Output(Bool())
  })

  val inst = withVoting{new Simple}

  inst.theseIos(1) := io.in
  io.out := inst.theseIos(0)
  io.valid := inst.io.ready
}

object GenExamp extends App {
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Demo())
}