default:
	sbt run

wave: default
	cd generated; iverilog -o Tes_tb Tes_tb.v
	cd generated; vvp Tes_tb
	cd generated; gtkwave test.vcd
