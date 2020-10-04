;buildInfoPackage: chisel3, version: 3.3.1, scalaVersion: 2.12.11, sbtVersion: 1.3.10
circuit Tes : 
  module Simple : 
    input clock : Clock
    input reset : Reset
    output io : {flip in : UInt<2>, out : UInt<2>}
    
    node _T = eq(io.in, UInt<1>("h00")) @[orbital.scala 19:13]
    io.out <= _T @[orbital.scala 19:10]
    
  module Simple_1 : 
    input clock : Clock
    input reset : Reset
    output io : {flip in : UInt<2>, out : UInt<2>}
    
    node _T = eq(io.in, UInt<1>("h00")) @[orbital.scala 19:13]
    io.out <= _T @[orbital.scala 19:10]
    
  module Precinct : 
    input clock : Clock
    input reset : Reset
    output io : {0 : {flip ioin : UInt<2>, ioout : UInt<2>}}
    
    inst mods_0 of Simple @[orbital.scala 29:54]
    mods_0.clock <= clock
    mods_0.reset <= reset
    inst mods_1 of Simple_1 @[orbital.scala 29:54]
    mods_1.clock <= clock
    mods_1.reset <= reset
    io.0.ioout <= mods_0.io.out @[orbital.scala 83:19]
    mods_0.io.in <= io.0.ioin @[orbital.scala 79:21]
    mods_1.io.in <= io.0.ioin @[orbital.scala 80:21]
    
  module Tes : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip in : UInt<2>, out : UInt<2>}
    
    inst inst of Precinct @[orbital.scala 130:21]
    inst.clock <= clock
    inst.reset <= reset
    inst.io.0.ioin <= io.in @[orbital.scala 161:20]
    io.out <= inst.io.0.ioout @[orbital.scala 162:10]
    
