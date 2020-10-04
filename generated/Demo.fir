;buildInfoPackage: chisel3, version: 3.3.1, scalaVersion: 2.12.11, sbtVersion: 1.3.10
circuit Demo : 
  module Simple : 
    input clock : Clock
    input reset : Reset
    output io : {flip in : UInt<2>, out : UInt<2>}
    
    node _T = add(io.in, UInt<1>("h01")) @[demo.scala 13:19]
    node _T_1 = tail(_T, 1) @[demo.scala 13:19]
    io.out <= _T_1 @[demo.scala 13:10]
    
  module Simple_1 : 
    input clock : Clock
    input reset : Reset
    output io : {flip in : UInt<2>, out : UInt<2>}
    
    node _T = add(io.in, UInt<1>("h01")) @[demo.scala 13:19]
    node _T_1 = tail(_T, 1) @[demo.scala 13:19]
    io.out <= _T_1 @[demo.scala 13:10]
    
  module BOE : 
    input clock : Clock
    input reset : Reset
    output io : {flip in1 : UInt<2>, flip in2 : UInt<2>, out : UInt<2>, valid : UInt<1>}
    
    node _T = eq(io.in1, io.in2) @[orbital.scala 110:16]
    when _T : @[orbital.scala 110:28]
      io.valid <= UInt<1>("h01") @[orbital.scala 111:14]
      skip @[orbital.scala 110:28]
    else : @[orbital.scala 113:14]
      io.valid <= UInt<1>("h00") @[orbital.scala 114:14]
      skip @[orbital.scala 113:14]
    io.out <= io.in1 @[orbital.scala 117:10]
    
  module Precinct : 
    input clock : Clock
    input reset : Reset
    output iosInner : {0 : {flip ioin : UInt<2>, ioout : UInt<2>}}
    output io : {ready : UInt<1>}
    
    inst mods_0 of Simple @[orbital.scala 20:54]
    mods_0.clock <= clock
    mods_0.reset <= reset
    inst mods_1 of Simple_1 @[orbital.scala 20:54]
    mods_1.clock <= clock
    mods_1.reset <= reset
    inst boe of BOE @[orbital.scala 68:19]
    boe.clock <= clock
    boe.reset <= reset
    io.ready <= boe.io.valid @[orbital.scala 74:12]
    boe.io.in1 <= mods_0.io.out @[orbital.scala 82:18]
    boe.io.in2 <= mods_1.io.out @[orbital.scala 83:18]
    iosInner.0.ioout <= boe.io.out @[orbital.scala 84:19]
    mods_0.io.in <= iosInner.0.ioin @[orbital.scala 78:21]
    mods_1.io.in <= iosInner.0.ioin @[orbital.scala 79:21]
    
  module Demo : 
    input clock : Clock
    input reset : UInt<1>
    output io : {flip in : UInt<2>, out : UInt<2>, valid : UInt<1>}
    
    inst inst of Precinct @[orbital.scala 92:21]
    inst.clock <= clock
    inst.reset <= reset
    inst.iosInner.0.ioin <= io.in @[demo.scala 25:20]
    io.out <= inst.iosInner.0.ioout @[demo.scala 26:10]
    io.valid <= inst.io.ready @[demo.scala 27:12]
    
