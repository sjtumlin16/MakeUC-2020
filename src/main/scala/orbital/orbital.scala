package orbital

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import chisel3.experimental.{BaseModule, DataMirror}
import chisel3.util.MixedVec
import scala.collection.immutable.ListMap
import scala.collection.mutable.ArrayBuffer
import scala.collection._
import scala.reflect.ClassTag

class ClonePorts (elts: Data*) extends Record {
    val elements = ListMap(elts.map(d => d.instanceName -> chiselTypeOf(d)): _*)
    def apply(field: String) = elements(field)
    override def cloneType = (new ClonePorts(elts: _*)).asInstanceOf[this.type]
}

class Precinct(val modules: Seq[() => BaseModule]) extends MultiIOModule {
  val mods: Seq[BaseModule] = modules.map(i => Module(i()))


  def bool2int(b:Boolean) = if (b) 1 else 0

  def getModulePorts(m: BaseModule): Seq[Data] = {
    val temp = DataMirror.fullModulePorts(m).map(_._2)
    temp.drop(3)
  }

  def getNumSubPorts(m: BaseModule): Int = {
    val temp = DataMirror.fullModulePorts(m).map(_._2)
    temp.drop(3).size
  }

  def getSubPorts(m: BaseModule): Seq[chisel3.Data] = {
    val temp = DataMirror.fullModulePorts(m).map(_._2)
    temp.drop(3)
  }

  def getSubPortDir(m: BaseModule): Seq[chisel3.ActualDirection] = {
    val temp = DataMirror.fullModulePorts(m).map(_._2).drop(3)
    temp.map({ case (subPort) => {DataMirror.directionOf(subPort)}})
  }

  def getSubPortDirBool(m: BaseModule): Seq[Int] = {
    getSubPortDir(m).map({ case (subPort) => {bool2int(subPort.toString == "Output")}})
  }

  val modIORecords: Seq[Record] = mods.map { mod => 
    new ClonePorts(getModulePorts(mod): _*)
  }.drop(1)

  val uniqueRecords = modIORecords.length
  println(uniqueRecords)

  var iosInner = IO(MixedVec(modIORecords))

  val tlIos = iosInner.map { case (toplevelRecord) => {
      (toplevelRecord.elements.values)}}

  val ios = mods.map { case (module) => {
      (getModulePorts(module))}}

  val module0Ios = ios(0).toSeq
  val module1Ios = ios(1).toSeq
  val theseIos = tlIos(0).toSeq

  val boe = Module(new BOE)

  val io = IO(new Bundle {
    val ready = Output(Bool())
  })

  io.ready := boe.io.valid

  for (i <- 0 until theseIos.length) {
    if (DataMirror.directionOf(module0Ios(i)).toString() == "Input") {
      module0Ios(i) := theseIos(i)
      module1Ios(i) := theseIos(i)
    }
    else {
      boe.io.in1 := module0Ios(i)
      boe.io.in2 := module1Ios(i)
      theseIos(i) := boe.io.out
    }
  }
}

object withVoting {
  def apply[T <: BaseModule](block: => T): Precinct = {

    val res = Module(
      new Precinct(Seq(
          () => block,
          () => block
      ))
  )

    res
  }
}

class BOE() extends Module {
  val io = IO(new Bundle {
    val in1 = Input(UInt(2.W))
    val in2 = Input(UInt(2.W))
    val out = Output(UInt(2.W))
    val valid = Output(Bool())
  })
  when (io.in1 === io.in2) {
    io.valid := true.B
  }
  .otherwise {
    io.valid := false.B
  }

  io.out := io.in1
}
