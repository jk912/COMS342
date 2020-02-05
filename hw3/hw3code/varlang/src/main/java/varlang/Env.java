package varlang;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an environment, which maps variables to values.
 *
 * @author hridesh
 *
 */
public interface Env {
	Value get (String search_var);

	@SuppressWarnings("serial")
	static public class LookupException extends RuntimeException {
		LookupException(String message){
			super(message);
		}
	}

	static public class EmptyEnv implements Env {
		public Value get (String search_var) {
			throw new LookupException("No binding found for name: " + search_var);
		}
	}

	static public class ExtendEnv implements Env {
		private Env _saved_env;
		private String _var;
		private Value _val;
		private double key;

		public ExtendEnv(Env saved_env, String var, Value val){
			_saved_env = saved_env;
			_var = var;
			_val = val;
		}

		public Value get (String search_var) {
			if (search_var.equals(_var))
				return _val;
			return _saved_env.get(search_var);
		}
	}

	static public class globalEnv implements Env {
		private List<String> _vars;
		private List<Value> _vals;
		private List<Double> _keys;
		private List<AST.VarExp> _locks;

		public globalEnv(){ init(); }

		@Override
		public Value get(String search_var) { return _vals.get(_vars.indexOf(search_var)); }
		public Value get(AST.VarExp lock) { return new Value.NumVal(_keys.get(_locks.indexOf(lock))); }

		void init(){
			_vars = new ArrayList<String>();
			_vals = new ArrayList<Value>();
			_keys = new ArrayList<Double>();
			_locks = new ArrayList<AST.VarExp>();
		}

		public void addVarVal(String var, Value val) { _vars.add(var); _vals.add(val); }
		public void addKeyLock(double key, AST.VarExp lock) { _keys.add(key); _locks.add(lock);}
	}
}