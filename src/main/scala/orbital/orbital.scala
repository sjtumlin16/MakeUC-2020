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

class Simple() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(2.W))
  })

  io.out := !io.in
}

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

  var io = IO(MixedVec(modIORecords))

  val tlIos = io.map { case (toplevelRecord) => {
      (toplevelRecord.elements.values)}}

  val ios = mods.map { case (module) => {
      (getModulePorts(module))}}

  val module0Ios = ios(0).toSeq
  val module1Ios = ios(1).toSeq
  val theseIos = tlIos(0).toSeq

  for (i <- 0 until theseIos.length) {
    if (DataMirror.directionOf(module0Ios(i)).toString() == "Input") {
      module0Ios(i) := theseIos(i)
      module1Ios(i) := theseIos(i)
    }
    else {
      theseIos(i) := module0Ios(i)
    }
  }
}




// class Precinct(block: => Simple) extends Module {
//   val io = IO(new Bundle {
//     val in = Input(UInt(2.W))
//     val out = Output(UInt(2.W))
//   })

//   val citizen1 = block
//   val citizen2 = block

//   citizen1.io.in := io.in
//   citizen2.io.in := io.in

//   val board = Module(new BOE())

//   board.io.in1 := citizen1.io.out
//   board.io.in2 := citizen2.io.out

//   io.out := board.io.out
// }


// object InstantiateAndConnectDriver extends App {
//   chisel3.Driver.execute(Array("--target-dir", "generated"), () => new InstantiateAndConnect(Seq(
//           () => new Multiplier,
//           () => new Multiplier
//       ))
//   )
// }

object withVoting {  // scalastyle:ignore object.name
  /** Creates a new Clock and Reset scope
    *
    * @param clock the new implicit Clock
    * @param reset the new implicit Reset
    * @param block the block of code to run with new implicit Clock and Reset
    * @return the result of the block
    */
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
  })

  io.out := io.in1
}

class Tes() extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(2.W))
    val out = Output(UInt(2.W))
  })

  val inst = withVoting{new Simple}

  // println(DataMirror.modulePorts(Module(new Simple)).map(_._2))

  inst.theseIos(1) := io.in
  io.out := inst.theseIos(0)
}

object GenExamp extends App {
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Tes())
  // val yer = Module(new Tes)
  // println(ClassTag(yer.getClass()))
}

/**
  * Compute GCD using subtraction method.
  * Subtracts the smaller from the larger until register y is zero.
  * value in register x is then the GCD
  */
// class BOE(citizens : Module) extends Module {
//   val io = IO(new Bundle {
//   	val muxAry = (0 until ports).map(x => Module(new FooBar(args))).toList // store the list in muxAry
//     val value1        = Input(UInt(16.W))
//   })

//   val citizen1 = citizens()

//   withClockAndReset



// muxAry(0).io.out := muxAry(1).io.in // use it however you want
//   val x  = Reg(UInt())
//   val y  = Reg(UInt())

//   when(x > y) { x := x - y }
//     .otherwise { y := y - x }

//   when(io.loadingValues) {
//     x := io.value1
//     y := io.value2
//   }

//   io.outputGCD := x
//   io.outputValid := y === 0.U
// }
