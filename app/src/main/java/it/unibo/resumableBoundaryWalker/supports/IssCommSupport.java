/**
 IssCommSupport.java
 ==========================================================================

 ==========================================================================
 */
package it.unibo.resumableBoundaryWalker.supports;

import it.unibo.resumableBoundaryWalker.interaction.IssObserver;
import it.unibo.resumableBoundaryWalker.interaction.IssOperations;

public interface IssCommSupport extends IssOperations {
    void registerObserver( IssObserver obs );
    void removeObserver( IssObserver obs );
    void close();
}
