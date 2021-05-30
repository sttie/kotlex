import SimpleIdsTokenType


data class Token(val lexeme: String, val type: SimpleIdsTokenType?, val start: Int)

class Kotlex {
	val a: Int = 0
	var input = ""
	var current = 0

	val kotlexTable = HashMap<Int, HashMap<Char, Int>>()
	fun set0State(): Unit {
		kotlexTable[0] = HashMap()
		kotlexTable[0]!!['l'] = 4
		kotlexTable[0]!!['H'] = 2
		kotlexTable[0]!!['b'] = 4
		kotlexTable[0]!!['m'] = 4
		kotlexTable[0]!!['1'] = 1
		kotlexTable[0]!!['v'] = 4
		kotlexTable[0]!!['i'] = 4
		kotlexTable[0]!!['y'] = 4
		kotlexTable[0]!!['p'] = 4
		kotlexTable[0]!!['o'] = 4
		kotlexTable[0]!!['W'] = 2
		kotlexTable[0]!!['0'] = 1
		kotlexTable[0]!!['z'] = 4
		kotlexTable[0]!!['C'] = 2
		kotlexTable[0]!!['J'] = 2
		kotlexTable[0]!!['h'] = 4
		kotlexTable[0]!!['c'] = 4
		kotlexTable[0]!!['x'] = 4
		kotlexTable[0]!!['8'] = 1
		kotlexTable[0]!!['d'] = 4
		kotlexTable[0]!!['Q'] = 2
		kotlexTable[0]!!['n'] = 4
		kotlexTable[0]!!['D'] = 2
		kotlexTable[0]!!['L'] = 2
		kotlexTable[0]!!['I'] = 2
		kotlexTable[0]!!['A'] = 2
		kotlexTable[0]!!['g'] = 4
		kotlexTable[0]!!['F'] = 2
		kotlexTable[0]!!['j'] = 4
		kotlexTable[0]!!['T'] = 2
		kotlexTable[0]!!['S'] = 2
		kotlexTable[0]!!['G'] = 2
		kotlexTable[0]!!['w'] = 4
		kotlexTable[0]!!['u'] = 4
		kotlexTable[0]!!['P'] = 2
		kotlexTable[0]!!['f'] = 4
		kotlexTable[0]!!['B'] = 2
		kotlexTable[0]!!['2'] = 1
		kotlexTable[0]!!['K'] = 2
		kotlexTable[0]!!['N'] = 2
		kotlexTable[0]!!['7'] = 1
		kotlexTable[0]!!['U'] = 2
		kotlexTable[0]!!['t'] = 4
		kotlexTable[0]!!['_'] = 3
		kotlexTable[0]!!['Z'] = 2
		kotlexTable[0]!!['3'] = 1
		kotlexTable[0]!!['e'] = 4
		kotlexTable[0]!!['r'] = 4
		kotlexTable[0]!!['E'] = 2
		kotlexTable[0]!!['R'] = 2
		kotlexTable[0]!!['M'] = 2
		kotlexTable[0]!!['5'] = 1
		kotlexTable[0]!!['Y'] = 2
		kotlexTable[0]!!['4'] = 1
		kotlexTable[0]!!['9'] = 1
		kotlexTable[0]!!['s'] = 4
		kotlexTable[0]!!['V'] = 2
		kotlexTable[0]!!['a'] = 4
		kotlexTable[0]!!['6'] = 1
		kotlexTable[0]!!['O'] = 2
		kotlexTable[0]!!['q'] = 4
		kotlexTable[0]!!['X'] = 2
		kotlexTable[0]!!['k'] = 4
	}

	fun set1State(): Unit {
		kotlexTable[1] = HashMap()
		kotlexTable[1]!!['2'] = 1
		kotlexTable[1]!!['1'] = 1
		kotlexTable[1]!!['3'] = 1
		kotlexTable[1]!!['5'] = 1
		kotlexTable[1]!!['8'] = 1
		kotlexTable[1]!!['7'] = 1
		kotlexTable[1]!!['4'] = 1
		kotlexTable[1]!!['6'] = 1
		kotlexTable[1]!!['0'] = 1
		kotlexTable[1]!!['9'] = 1
	}

	fun set2State(): Unit {
		kotlexTable[2] = HashMap()
		kotlexTable[2]!!['j'] = 8
		kotlexTable[2]!!['M'] = 6
		kotlexTable[2]!!['H'] = 6
		kotlexTable[2]!!['p'] = 8
		kotlexTable[2]!!['B'] = 6
		kotlexTable[2]!!['A'] = 6
		kotlexTable[2]!!['S'] = 6
		kotlexTable[2]!!['h'] = 8
		kotlexTable[2]!!['c'] = 8
		kotlexTable[2]!!['v'] = 8
		kotlexTable[2]!!['x'] = 8
		kotlexTable[2]!!['k'] = 8
		kotlexTable[2]!!['K'] = 6
		kotlexTable[2]!!['8'] = 5
		kotlexTable[2]!!['I'] = 6
		kotlexTable[2]!!['n'] = 8
		kotlexTable[2]!!['q'] = 8
		kotlexTable[2]!!['e'] = 8
		kotlexTable[2]!!['_'] = 7
		kotlexTable[2]!!['z'] = 8
		kotlexTable[2]!!['2'] = 5
		kotlexTable[2]!!['Q'] = 6
		kotlexTable[2]!!['N'] = 6
		kotlexTable[2]!!['a'] = 8
		kotlexTable[2]!!['Y'] = 6
		kotlexTable[2]!!['u'] = 8
		kotlexTable[2]!!['6'] = 5
		kotlexTable[2]!!['Z'] = 6
		kotlexTable[2]!!['T'] = 6
		kotlexTable[2]!!['7'] = 5
		kotlexTable[2]!!['U'] = 6
		kotlexTable[2]!!['t'] = 8
		kotlexTable[2]!!['D'] = 6
		kotlexTable[2]!!['G'] = 6
		kotlexTable[2]!!['l'] = 8
		kotlexTable[2]!!['m'] = 8
		kotlexTable[2]!!['E'] = 6
		kotlexTable[2]!!['g'] = 8
		kotlexTable[2]!!['f'] = 8
		kotlexTable[2]!!['X'] = 6
		kotlexTable[2]!!['s'] = 8
		kotlexTable[2]!!['J'] = 6
		kotlexTable[2]!!['W'] = 6
		kotlexTable[2]!!['o'] = 8
		kotlexTable[2]!!['F'] = 6
		kotlexTable[2]!!['y'] = 8
		kotlexTable[2]!!['1'] = 5
		kotlexTable[2]!!['b'] = 8
		kotlexTable[2]!!['O'] = 6
		kotlexTable[2]!!['V'] = 6
		kotlexTable[2]!!['w'] = 8
		kotlexTable[2]!!['5'] = 5
		kotlexTable[2]!!['R'] = 6
		kotlexTable[2]!!['3'] = 5
		kotlexTable[2]!!['9'] = 5
		kotlexTable[2]!!['P'] = 6
		kotlexTable[2]!!['4'] = 5
		kotlexTable[2]!!['d'] = 8
		kotlexTable[2]!!['r'] = 8
		kotlexTable[2]!!['L'] = 6
		kotlexTable[2]!!['i'] = 8
		kotlexTable[2]!!['0'] = 5
		kotlexTable[2]!!['C'] = 6
	}

	fun set3State(): Unit {
		kotlexTable[3] = HashMap()
		kotlexTable[3]!!['J'] = 6
		kotlexTable[3]!!['S'] = 6
		kotlexTable[3]!!['7'] = 5
		kotlexTable[3]!!['V'] = 6
		kotlexTable[3]!!['P'] = 6
		kotlexTable[3]!!['Y'] = 6
		kotlexTable[3]!!['w'] = 8
		kotlexTable[3]!!['N'] = 6
		kotlexTable[3]!!['q'] = 8
		kotlexTable[3]!!['E'] = 6
		kotlexTable[3]!!['r'] = 8
		kotlexTable[3]!!['M'] = 6
		kotlexTable[3]!!['4'] = 5
		kotlexTable[3]!!['F'] = 6
		kotlexTable[3]!!['5'] = 5
		kotlexTable[3]!!['z'] = 8
		kotlexTable[3]!!['6'] = 5
		kotlexTable[3]!!['a'] = 8
		kotlexTable[3]!!['n'] = 8
		kotlexTable[3]!!['p'] = 8
		kotlexTable[3]!!['T'] = 6
		kotlexTable[3]!!['t'] = 8
		kotlexTable[3]!!['h'] = 8
		kotlexTable[3]!!['Z'] = 6
		kotlexTable[3]!!['i'] = 8
		kotlexTable[3]!!['L'] = 6
		kotlexTable[3]!!['s'] = 8
		kotlexTable[3]!!['v'] = 8
		kotlexTable[3]!!['u'] = 8
		kotlexTable[3]!!['Q'] = 6
		kotlexTable[3]!!['G'] = 6
		kotlexTable[3]!!['I'] = 6
		kotlexTable[3]!!['_'] = 7
		kotlexTable[3]!!['b'] = 8
		kotlexTable[3]!!['m'] = 8
		kotlexTable[3]!!['g'] = 8
		kotlexTable[3]!!['y'] = 8
		kotlexTable[3]!!['e'] = 8
		kotlexTable[3]!!['D'] = 6
		kotlexTable[3]!!['O'] = 6
		kotlexTable[3]!!['U'] = 6
		kotlexTable[3]!!['C'] = 6
		kotlexTable[3]!!['H'] = 6
		kotlexTable[3]!!['B'] = 6
		kotlexTable[3]!!['9'] = 5
		kotlexTable[3]!!['k'] = 8
		kotlexTable[3]!!['A'] = 6
		kotlexTable[3]!!['l'] = 8
		kotlexTable[3]!!['3'] = 5
		kotlexTable[3]!!['c'] = 8
		kotlexTable[3]!!['d'] = 8
		kotlexTable[3]!!['o'] = 8
		kotlexTable[3]!!['0'] = 5
		kotlexTable[3]!!['W'] = 6
		kotlexTable[3]!!['X'] = 6
		kotlexTable[3]!!['f'] = 8
		kotlexTable[3]!!['j'] = 8
		kotlexTable[3]!!['8'] = 5
		kotlexTable[3]!!['x'] = 8
		kotlexTable[3]!!['K'] = 6
		kotlexTable[3]!!['2'] = 5
		kotlexTable[3]!!['R'] = 6
		kotlexTable[3]!!['1'] = 5
	}

	fun set4State(): Unit {
		kotlexTable[4] = HashMap()
		kotlexTable[4]!!['7'] = 5
		kotlexTable[4]!!['q'] = 8
		kotlexTable[4]!!['O'] = 6
		kotlexTable[4]!!['4'] = 5
		kotlexTable[4]!!['5'] = 5
		kotlexTable[4]!!['s'] = 8
		kotlexTable[4]!!['J'] = 6
		kotlexTable[4]!!['S'] = 6
		kotlexTable[4]!!['G'] = 6
		kotlexTable[4]!!['N'] = 6
		kotlexTable[4]!!['r'] = 8
		kotlexTable[4]!!['z'] = 8
		kotlexTable[4]!!['b'] = 8
		kotlexTable[4]!!['h'] = 8
		kotlexTable[4]!!['n'] = 8
		kotlexTable[4]!!['0'] = 5
		kotlexTable[4]!!['Q'] = 6
		kotlexTable[4]!!['K'] = 6
		kotlexTable[4]!!['X'] = 6
		kotlexTable[4]!!['U'] = 6
		kotlexTable[4]!!['Y'] = 6
		kotlexTable[4]!!['e'] = 8
		kotlexTable[4]!!['8'] = 5
		kotlexTable[4]!!['_'] = 7
		kotlexTable[4]!!['E'] = 6
		kotlexTable[4]!!['W'] = 6
		kotlexTable[4]!!['B'] = 6
		kotlexTable[4]!!['y'] = 8
		kotlexTable[4]!!['3'] = 5
		kotlexTable[4]!!['I'] = 6
		kotlexTable[4]!!['C'] = 6
		kotlexTable[4]!!['a'] = 8
		kotlexTable[4]!!['L'] = 6
		kotlexTable[4]!!['F'] = 6
		kotlexTable[4]!!['f'] = 8
		kotlexTable[4]!!['6'] = 5
		kotlexTable[4]!!['d'] = 8
		kotlexTable[4]!!['x'] = 8
		kotlexTable[4]!!['H'] = 6
		kotlexTable[4]!!['o'] = 8
		kotlexTable[4]!!['u'] = 8
		kotlexTable[4]!!['D'] = 6
		kotlexTable[4]!!['1'] = 5
		kotlexTable[4]!!['Z'] = 6
		kotlexTable[4]!!['P'] = 6
		kotlexTable[4]!!['p'] = 8
		kotlexTable[4]!!['i'] = 8
		kotlexTable[4]!!['k'] = 8
		kotlexTable[4]!!['T'] = 6
		kotlexTable[4]!!['A'] = 6
		kotlexTable[4]!!['l'] = 8
		kotlexTable[4]!!['V'] = 6
		kotlexTable[4]!!['9'] = 5
		kotlexTable[4]!!['M'] = 6
		kotlexTable[4]!!['v'] = 8
		kotlexTable[4]!!['R'] = 6
		kotlexTable[4]!!['t'] = 8
		kotlexTable[4]!!['m'] = 8
		kotlexTable[4]!!['2'] = 5
		kotlexTable[4]!!['j'] = 8
		kotlexTable[4]!!['c'] = 8
		kotlexTable[4]!!['g'] = 8
		kotlexTable[4]!!['w'] = 8
	}

	fun set5State(): Unit {
		kotlexTable[5] = HashMap()
		kotlexTable[5]!!['S'] = 6
		kotlexTable[5]!!['p'] = 8
		kotlexTable[5]!!['d'] = 8
		kotlexTable[5]!!['3'] = 5
		kotlexTable[5]!!['E'] = 6
		kotlexTable[5]!!['m'] = 8
		kotlexTable[5]!!['K'] = 6
		kotlexTable[5]!!['1'] = 5
		kotlexTable[5]!!['w'] = 8
		kotlexTable[5]!!['H'] = 6
		kotlexTable[5]!!['b'] = 8
		kotlexTable[5]!!['D'] = 6
		kotlexTable[5]!!['c'] = 8
		kotlexTable[5]!!['F'] = 6
		kotlexTable[5]!!['n'] = 8
		kotlexTable[5]!!['r'] = 8
		kotlexTable[5]!!['J'] = 6
		kotlexTable[5]!!['O'] = 6
		kotlexTable[5]!!['T'] = 6
		kotlexTable[5]!!['X'] = 6
		kotlexTable[5]!!['U'] = 6
		kotlexTable[5]!!['9'] = 5
		kotlexTable[5]!!['v'] = 8
		kotlexTable[5]!!['h'] = 8
		kotlexTable[5]!!['l'] = 8
		kotlexTable[5]!!['2'] = 5
		kotlexTable[5]!!['C'] = 6
		kotlexTable[5]!!['k'] = 8
		kotlexTable[5]!!['4'] = 5
		kotlexTable[5]!!['7'] = 5
		kotlexTable[5]!!['G'] = 6
		kotlexTable[5]!!['a'] = 8
		kotlexTable[5]!!['Z'] = 6
		kotlexTable[5]!!['e'] = 8
		kotlexTable[5]!!['z'] = 8
		kotlexTable[5]!!['f'] = 8
		kotlexTable[5]!!['B'] = 6
		kotlexTable[5]!!['L'] = 6
		kotlexTable[5]!!['q'] = 8
		kotlexTable[5]!!['0'] = 5
		kotlexTable[5]!!['R'] = 6
		kotlexTable[5]!!['j'] = 8
		kotlexTable[5]!!['Y'] = 6
		kotlexTable[5]!!['6'] = 5
		kotlexTable[5]!!['u'] = 8
		kotlexTable[5]!!['M'] = 6
		kotlexTable[5]!!['s'] = 8
		kotlexTable[5]!!['A'] = 6
		kotlexTable[5]!!['I'] = 6
		kotlexTable[5]!!['y'] = 8
		kotlexTable[5]!!['_'] = 7
		kotlexTable[5]!!['i'] = 8
		kotlexTable[5]!!['N'] = 6
		kotlexTable[5]!!['V'] = 6
		kotlexTable[5]!!['P'] = 6
		kotlexTable[5]!!['t'] = 8
		kotlexTable[5]!!['x'] = 8
		kotlexTable[5]!!['Q'] = 6
		kotlexTable[5]!!['8'] = 5
		kotlexTable[5]!!['5'] = 5
		kotlexTable[5]!!['W'] = 6
		kotlexTable[5]!!['o'] = 8
		kotlexTable[5]!!['g'] = 8
	}

	fun set6State(): Unit {
		kotlexTable[6] = HashMap()
		kotlexTable[6]!!['p'] = 8
		kotlexTable[6]!!['5'] = 5
		kotlexTable[6]!!['c'] = 8
		kotlexTable[6]!!['q'] = 8
		kotlexTable[6]!!['d'] = 8
		kotlexTable[6]!!['H'] = 6
		kotlexTable[6]!!['Q'] = 6
		kotlexTable[6]!!['K'] = 6
		kotlexTable[6]!!['P'] = 6
		kotlexTable[6]!!['o'] = 8
		kotlexTable[6]!!['O'] = 6
		kotlexTable[6]!!['V'] = 6
		kotlexTable[6]!!['9'] = 5
		kotlexTable[6]!!['8'] = 5
		kotlexTable[6]!!['X'] = 6
		kotlexTable[6]!!['G'] = 6
		kotlexTable[6]!!['1'] = 5
		kotlexTable[6]!!['N'] = 6
		kotlexTable[6]!!['Y'] = 6
		kotlexTable[6]!!['t'] = 8
		kotlexTable[6]!!['b'] = 8
		kotlexTable[6]!!['i'] = 8
		kotlexTable[6]!!['0'] = 5
		kotlexTable[6]!!['S'] = 6
		kotlexTable[6]!!['4'] = 5
		kotlexTable[6]!!['D'] = 6
		kotlexTable[6]!!['2'] = 5
		kotlexTable[6]!!['A'] = 6
		kotlexTable[6]!!['r'] = 8
		kotlexTable[6]!!['f'] = 8
		kotlexTable[6]!!['n'] = 8
		kotlexTable[6]!!['g'] = 8
		kotlexTable[6]!!['C'] = 6
		kotlexTable[6]!!['3'] = 5
		kotlexTable[6]!!['Z'] = 6
		kotlexTable[6]!!['I'] = 6
		kotlexTable[6]!!['T'] = 6
		kotlexTable[6]!!['x'] = 8
		kotlexTable[6]!!['u'] = 8
		kotlexTable[6]!!['7'] = 5
		kotlexTable[6]!!['U'] = 6
		kotlexTable[6]!!['h'] = 8
		kotlexTable[6]!!['F'] = 6
		kotlexTable[6]!!['L'] = 6
		kotlexTable[6]!!['m'] = 8
		kotlexTable[6]!!['z'] = 8
		kotlexTable[6]!!['a'] = 8
		kotlexTable[6]!!['M'] = 6
		kotlexTable[6]!!['E'] = 6
		kotlexTable[6]!!['y'] = 8
		kotlexTable[6]!!['v'] = 8
		kotlexTable[6]!!['W'] = 6
		kotlexTable[6]!!['e'] = 8
		kotlexTable[6]!!['j'] = 8
		kotlexTable[6]!!['_'] = 7
		kotlexTable[6]!!['B'] = 6
		kotlexTable[6]!!['J'] = 6
		kotlexTable[6]!!['w'] = 8
		kotlexTable[6]!!['s'] = 8
		kotlexTable[6]!!['6'] = 5
		kotlexTable[6]!!['l'] = 8
		kotlexTable[6]!!['R'] = 6
		kotlexTable[6]!!['k'] = 8
	}

	fun set7State(): Unit {
		kotlexTable[7] = HashMap()
		kotlexTable[7]!!['j'] = 8
		kotlexTable[7]!!['D'] = 6
		kotlexTable[7]!!['z'] = 8
		kotlexTable[7]!!['3'] = 5
		kotlexTable[7]!!['i'] = 8
		kotlexTable[7]!!['V'] = 6
		kotlexTable[7]!!['B'] = 6
		kotlexTable[7]!!['P'] = 6
		kotlexTable[7]!!['L'] = 6
		kotlexTable[7]!!['X'] = 6
		kotlexTable[7]!!['2'] = 5
		kotlexTable[7]!!['o'] = 8
		kotlexTable[7]!!['k'] = 8
		kotlexTable[7]!!['1'] = 5
		kotlexTable[7]!!['r'] = 8
		kotlexTable[7]!!['a'] = 8
		kotlexTable[7]!!['d'] = 8
		kotlexTable[7]!!['h'] = 8
		kotlexTable[7]!!['6'] = 5
		kotlexTable[7]!!['g'] = 8
		kotlexTable[7]!!['Q'] = 6
		kotlexTable[7]!!['t'] = 8
		kotlexTable[7]!!['7'] = 5
		kotlexTable[7]!!['_'] = 7
		kotlexTable[7]!!['p'] = 8
		kotlexTable[7]!!['u'] = 8
		kotlexTable[7]!!['Z'] = 6
		kotlexTable[7]!!['w'] = 8
		kotlexTable[7]!!['x'] = 8
		kotlexTable[7]!!['S'] = 6
		kotlexTable[7]!!['f'] = 8
		kotlexTable[7]!!['H'] = 6
		kotlexTable[7]!!['J'] = 6
		kotlexTable[7]!!['N'] = 6
		kotlexTable[7]!!['Y'] = 6
		kotlexTable[7]!!['M'] = 6
		kotlexTable[7]!!['s'] = 8
		kotlexTable[7]!!['O'] = 6
		kotlexTable[7]!!['F'] = 6
		kotlexTable[7]!!['A'] = 6
		kotlexTable[7]!!['4'] = 5
		kotlexTable[7]!!['l'] = 8
		kotlexTable[7]!!['m'] = 8
		kotlexTable[7]!!['5'] = 5
		kotlexTable[7]!!['T'] = 6
		kotlexTable[7]!!['v'] = 8
		kotlexTable[7]!!['q'] = 8
		kotlexTable[7]!!['b'] = 8
		kotlexTable[7]!!['E'] = 6
		kotlexTable[7]!!['9'] = 5
		kotlexTable[7]!!['R'] = 6
		kotlexTable[7]!!['G'] = 6
		kotlexTable[7]!!['n'] = 8
		kotlexTable[7]!!['e'] = 8
		kotlexTable[7]!!['y'] = 8
		kotlexTable[7]!!['8'] = 5
		kotlexTable[7]!!['W'] = 6
		kotlexTable[7]!!['0'] = 5
		kotlexTable[7]!!['K'] = 6
		kotlexTable[7]!!['U'] = 6
		kotlexTable[7]!!['c'] = 8
		kotlexTable[7]!!['C'] = 6
		kotlexTable[7]!!['I'] = 6
	}

	fun set8State(): Unit {
		kotlexTable[8] = HashMap()
		kotlexTable[8]!!['B'] = 6
		kotlexTable[8]!!['3'] = 5
		kotlexTable[8]!!['I'] = 6
		kotlexTable[8]!!['b'] = 8
		kotlexTable[8]!!['h'] = 8
		kotlexTable[8]!!['2'] = 5
		kotlexTable[8]!!['d'] = 8
		kotlexTable[8]!!['t'] = 8
		kotlexTable[8]!!['C'] = 6
		kotlexTable[8]!!['m'] = 8
		kotlexTable[8]!!['H'] = 6
		kotlexTable[8]!!['Y'] = 6
		kotlexTable[8]!!['N'] = 6
		kotlexTable[8]!!['a'] = 8
		kotlexTable[8]!!['K'] = 6
		kotlexTable[8]!!['g'] = 8
		kotlexTable[8]!!['L'] = 6
		kotlexTable[8]!!['j'] = 8
		kotlexTable[8]!!['4'] = 5
		kotlexTable[8]!!['q'] = 8
		kotlexTable[8]!!['R'] = 6
		kotlexTable[8]!!['i'] = 8
		kotlexTable[8]!!['9'] = 5
		kotlexTable[8]!!['F'] = 6
		kotlexTable[8]!!['J'] = 6
		kotlexTable[8]!!['p'] = 8
		kotlexTable[8]!!['v'] = 8
		kotlexTable[8]!!['6'] = 5
		kotlexTable[8]!!['c'] = 8
		kotlexTable[8]!!['U'] = 6
		kotlexTable[8]!!['G'] = 6
		kotlexTable[8]!!['D'] = 6
		kotlexTable[8]!!['_'] = 7
		kotlexTable[8]!!['A'] = 6
		kotlexTable[8]!!['k'] = 8
		kotlexTable[8]!!['X'] = 6
		kotlexTable[8]!!['x'] = 8
		kotlexTable[8]!!['7'] = 5
		kotlexTable[8]!!['n'] = 8
		kotlexTable[8]!!['y'] = 8
		kotlexTable[8]!!['T'] = 6
		kotlexTable[8]!!['z'] = 8
		kotlexTable[8]!!['P'] = 6
		kotlexTable[8]!!['s'] = 8
		kotlexTable[8]!!['1'] = 5
		kotlexTable[8]!!['0'] = 5
		kotlexTable[8]!!['l'] = 8
		kotlexTable[8]!!['8'] = 5
		kotlexTable[8]!!['f'] = 8
		kotlexTable[8]!!['5'] = 5
		kotlexTable[8]!!['r'] = 8
		kotlexTable[8]!!['V'] = 6
		kotlexTable[8]!!['e'] = 8
		kotlexTable[8]!!['O'] = 6
		kotlexTable[8]!!['M'] = 6
		kotlexTable[8]!!['Q'] = 6
		kotlexTable[8]!!['u'] = 8
		kotlexTable[8]!!['W'] = 6
		kotlexTable[8]!!['Z'] = 6
		kotlexTable[8]!!['S'] = 6
		kotlexTable[8]!!['o'] = 8
		kotlexTable[8]!!['w'] = 8
		kotlexTable[8]!!['E'] = 6
	}

	init {
		set0State()
		set1State()
		set2State()
		set3State()
		set4State()
		set5State()
		set6State()
		set7State()
		set8State()
	}

	fun reset(input_: String): Unit {
		input = input_
		current = 0
	}

	fun getToken(): Token? {
		var currentState = 0
		var start = current
		val acceptingStatesToTypes = hashMapOf(
			1 to SimpleIdsTokenType.NUMBER, 2 to SimpleIdsTokenType.ID, 3 to SimpleIdsTokenType.ID,
			4 to SimpleIdsTokenType.ID, 5 to SimpleIdsTokenType.ID, 6 to SimpleIdsTokenType.ID,
			7 to SimpleIdsTokenType.ID, 8 to SimpleIdsTokenType.ID,
		)
		
		while (true) {
			if (current >= input.length) return null

			var newState = if (current < input.length) (kotlexTable[currentState] ?: HashMap()).getOrDefault(input[current], -1) else -1
			
			if (newState == -1 && currentState in acceptingStatesToTypes) {
				return Token(input.substring(start until current), acceptingStatesToTypes[currentState], start)
			}
			else if (newState == -1 && start == current) {
				start++
			}
			else if (newState == -1) {
				throw IllegalStateException("Undefined token!")
			}
			else {
				currentState = newState
			}
			current++
		}
	}

}
