// // Requires Chisel 3.2+
// package tester

// import chisel3.experimental.{BaseModule, MultiIOModule, DataMirror}
// import chisel3.util.MixedVec
// import scala.collection.immutable.ListMap

// class ClonePorts (elts: Data*) extends Record {
//     val elements = ListMap(elts.map(d => d.instanceName -> chiselTypeOf(d)): _*)
//     def apply(field: String) = elements(field)
//     override def cloneType = (new ClonePorts(elts: _*)).asInstanceOf[this.type]
// }

// /**
//   * This consumes a list of module (constructors) and instantiates all the modules
//   * in parallel, creates top-level IOs (records) for each of them and wires them up.
//   * Note that this loses static typing for the IOs for each module.
//   * To access them, you can use something like myWrapper(0)("io_myport") := ...
//   */
// class MultiModuleWrapper(val modules: Seq[() => BaseModule]) extends MultiIOModule {
//   // Instantiate all modules
//   val mods: Seq[BaseModule] = modules.map(i => Module(i()))

//   def getModulePorts(m: BaseModule): Seq[Data] = {
//     DataMirror.modulePorts(m).map(_._2)
//   }
//   // Create a Seq of records where each record is the IO for one module
//   val modIORecords: Seq[Record] = mods.map { mod => 
//     new ClonePorts(getModulePorts(mod): _*)
//   }

//   // Create an IO for all modules
//   val modIOs = IO(MixedVec(modIORecords))

//   // Wire up module IOs and top-level IOs
//   (modIOs zip mods).foreach { case (toplevelRecord, module) => {
//       (toplevelRecord.elements.values zip getModulePorts(module)).foreach { case (a, b) => a <> b }
//   } }
// }

// class Inv extends MultiIOModule {
//     val in = IO(Input(Bool()))
//     val out = IO(Output(Bool()))
//     out := ~in
// }

// class Adder extends MultiIOModule {
//     val a = IO(Input(UInt(8.W)))
//     val b = IO(Input(UInt(8.W)))
//     val c = IO(Output(UInt(8.W)))
//     c := a +& b
// }

// class Multiplier extends Module {
//     val io = IO(new Bundle {
//         val a = Input(UInt(4.W))
//         val b = Input(UInt(4.W))
//         val c = Output(UInt(8.W))
//     })
//     io.c := io.a * io.b
// }

// // object cool extends App {
//   println(getVerilog(
//       new MultiModuleWrapper(Seq(
//           () => new Inv,
//           () => new Adder,
//           () => new Multiplier
//       ))
//   ))
