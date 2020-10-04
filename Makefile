default:
	sbt run

wave: default
	cd generated; iverilog -o Demo_tb Demo_tb.v
	cd generated; vvp Demo_tb
	cd generated; gtkwave wav.vcd
