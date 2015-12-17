package com.dumbpug.vem.ScriptInterface;

import org.mozilla.javascript.*;

public class MyFactory extends ContextFactory
{
	/**
	 * 
	 * @author Nikolas Howard
	 *
	 */
	@SuppressWarnings("deprecation")
	class StoppableContext extends Context {
		private ContextStopper contextStopper = null;
		
		public void setContextStopper(ContextStopper cs) {
			contextStopper = cs;
		}
		
		public boolean isRequiredToStop() {
			return contextStopper.isStopPending();
		}
	}
	
    // Override makeContext()
    protected Context makeContext()
    {
        StoppableContext cx = new StoppableContext();
        // Use pure interpreter mode to allow for
        // observeInstructionCount(Context, int) to work
        cx.setOptimizationLevel(-1);
        // Make Rhino runtime to call observeInstructionCount
        // each bytecode instruction
        cx.setInstructionObserverThreshold(1);
        return cx;
    }

    // Override hasFeature(Context, int)
    public boolean hasFeature(Context cx, int featureIndex)
    {
        // Turn on maximum compatibility with MSIE scripts
        switch (featureIndex) {
            case Context.FEATURE_NON_ECMA_GET_YEAR:
                return true;

            case Context.FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME:
                return true;

            case Context.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER:
                return true;

            case Context.FEATURE_PARENT_PROTO_PROPERTIES:
                return false;
        }
        return super.hasFeature(cx, featureIndex);
    }

    // Override observeInstructionCount(Context, int)
    protected void observeInstructionCount(Context cx, int instructionCount)
    {
    	if(((StoppableContext) cx).isRequiredToStop()) {
    		throw new RuntimeException("stopped");
    	}
    }
}