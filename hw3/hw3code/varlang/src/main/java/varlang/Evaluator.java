package varlang;
import static varlang.AST.*;
import static varlang.Value.*;

import java.util.List;
import java.util.ArrayList;

import varlang.AST.AddExp;
import varlang.AST.NumExp;
import varlang.AST.DivExp;
import varlang.AST.MultExp;
import varlang.AST.Program;
import varlang.AST.SubExp;
import varlang.AST.VarExp;
import varlang.AST.LeteExp;
import varlang.AST.DecExp;
import varlang.AST.Visitor;
import varlang.Env.EmptyEnv;
import varlang.Env.ExtendEnv;

public class Evaluator implements Visitor<Value> {

	Env global_env = new EmptyEnv();
	List<String> names = new ArrayList<String>();
	List<Value> values = new ArrayList<Value>();

	Value valueOf(Program p) {
		Env env = new EmptyEnv();
		// Value of a program in this language is the value of the expression
		return (Value) p.accept(this, env);
	}

	// changing the env is what causes the "no name for a found", its also
	@Override
	public Value visit(AddExp e, Env env) {
		List<Exp> operands = e.all();
		double result = 0;
		for(Exp exp: operands) {
			NumVal intermediate;
			if (exp instanceof VarExp && names.contains(((VarExp) exp)._name)) {
				intermediate = (NumVal) values.get(names.indexOf(((VarExp) exp)._name));
			}
			else {
				try {
					intermediate = (NumVal) exp.accept(this, env); // Dynamic type-checking
				} catch (Exception a) {
					intermediate = (NumVal) env.get(exp.accept(this, env) + "");
				}
			}
			result += intermediate.v(); //Semantics of AddExp in terms of the target language.
		}
		return new NumVal(result);
	}

	@Override
	public Value visit(NumExp e, Env env) {
		return new NumVal(e.v());
	}

	@Override
	public Value visit(DivExp e, Env env) {
		List<Exp> operands = e.all();
		NumVal lVal = (NumVal) operands.get(0).accept(this, env);
		double result = lVal.v();
		for(int i=1; i<operands.size(); i++) {
			NumVal rVal;
			if (operands.get(i) instanceof VarExp && names.contains(((VarExp) operands.get(i))._name)) {
				rVal = (NumVal) values.get(names.indexOf(((VarExp) operands.get(i))._name));
			}
			else rVal = (NumVal) operands.get(i).accept(this, env);
			result = result / rVal.v();
		}
		return new NumVal(result);
	}

	@Override
	public Value visit(MultExp e, Env env) {
		List<Exp> operands = e.all();
		double result = 1;
		for(Exp exp: operands) {
			NumVal intermediate;
			if (exp instanceof VarExp && names.contains(((VarExp) exp)._name)) {
				intermediate = (NumVal) values.get(names.indexOf(((VarExp) exp)._name));
			}
			else intermediate = (NumVal) exp.accept(this, env); // Dynamic type-checking
			result *= intermediate.v(); //Semantics of MultExp.
		}
		return new NumVal(result);
	}

	@Override
	public Value visit(Program p, Env env) {
		return (Value) p.e().accept(this, env);
	}

	@Override
	public Value visit(SubExp e, Env env) {
		List<Exp> operands = e.all();
		NumVal lVal = (NumVal) operands.get(0).accept(this, env);
		double result = lVal.v();
		for(int i=1; i<operands.size(); i++) {
			NumVal rVal;
			if (operands.get(i) instanceof VarExp && names.contains(((VarExp) operands.get(i))._name)) {
				rVal = (NumVal) values.get(names.indexOf(((VarExp) operands.get(i))._name));
			}
			else rVal = (NumVal) operands.get(i).accept(this, env);
			result = result - rVal.v();
		}
		return new NumVal(result);
	}

	@Override
	public Value visit(VarExp e, Env env) {
		// Previously, all variables had value 42. New semantics.
		return env.get(e.name());
	}

	// My Code
	// (define month 9)
	// (let ((x 1)) (+ x month))
	@Override
	public Value visit(DefExp e, Env env) {
		List<Exp> value_exps = e.value_exps();
		int count = 0;
		for (Exp exp : value_exps) {
			names.add(e.names().get(count));
			values.add((Value) exp.accept(this, global_env));
			global_env = new ExtendEnv(global_env, e.names().get(count), (Value) exp.accept(this, global_env));
			count++;
		}
		return new UnitVal();
	}

	// (let ((a 3) (b a) (c (+ a b))) c)
	// (let ((a 3) (b z) (c (+ a b))) c)
	@Override
	public Value visit(LetExp e, Env env) { // New for varlang.
		List<String> names = e.names();
		List<Exp> value_exps = e.value_exps();
		Value current = null;
		int count = 0;

		for (Exp exp : value_exps){
			if (exp instanceof VarExp && names.contains(((VarExp) exp)._name)) {
				current = (NumVal) values.get(names.indexOf(((VarExp) exp)._name));
			}
			current = (Value) exp.accept(this, env);
			env = new ExtendEnv(env, names.get(count), current);
			count++;
		}
		return (Value) e.body().accept(this, env);
	}

	// input includes a key(int) that is used int the dec stmt
	// (lete 2 ((x 1)) x)
	@Override
	public Value visit(LeteExp e, Env env){
		double key = ((NumExp) e.key())._val;
		System.out.println(e._names.get(0));
		System.out.println(((NumExp) e._value_exps.get(0))._val);
		System.out.println("Checkpoint: Evaluator.visit(LeteExp)");
		return new UnitVal();
	}

	// (lete 20 ((x 1)) (dec 20 x))
	@Override
	public Value visit(DecExp e, Env env){
		System.out.println("Checkpoint: Evaluator.visit(LeteExp)");
		return new UnitVal();
	}
	// End
}