package org.ovirt.engine.core.searchbackend;

public class VdsCrossRefAutoCompleter extends SearchObjectsBaseAutoCompleter {
    public VdsCrossRefAutoCompleter() {
        mVerbs.put(SearchObjects.VM_PLU_OBJ_NAME, SearchObjects.VM_PLU_OBJ_NAME);
        mVerbs.put(SearchObjects.TEMPLATE_PLU_OBJ_NAME, SearchObjects.TEMPLATE_PLU_OBJ_NAME);
        mVerbs.put(SearchObjects.AUDIT_PLU_OBJ_NAME, SearchObjects.AUDIT_PLU_OBJ_NAME);
        mVerbs.put(SearchObjects.VDC_USER_PLU_OBJ_NAME, SearchObjects.VDC_USER_PLU_OBJ_NAME);
        mVerbs.put(SearchObjects.VDC_STORAGE_DOMAIN_OBJ_NAME, SearchObjects.VDC_STORAGE_DOMAIN_OBJ_NAME);
        mVerbs.put(SearchObjects.VDS_NETWORK_INTERFACE_OBJ_NAME, SearchObjects.VDS_NETWORK_INTERFACE_OBJ_NAME);
        buildCompletions();
        mVerbs.put(SearchObjects.VM_OBJ_NAME, SearchObjects.VM_OBJ_NAME);
        mVerbs.put(SearchObjects.TEMPLATE_OBJ_NAME, SearchObjects.TEMPLATE_OBJ_NAME);
        mVerbs.put(SearchObjects.AUDIT_OBJ_NAME, SearchObjects.AUDIT_OBJ_NAME);
        mVerbs.put(SearchObjects.VDC_USER_OBJ_NAME, SearchObjects.VDC_USER_OBJ_NAME);
    }
}
