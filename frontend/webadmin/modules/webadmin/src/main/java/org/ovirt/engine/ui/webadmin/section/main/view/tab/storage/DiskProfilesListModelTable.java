package org.ovirt.engine.ui.webadmin.section.main.view.tab.storage;

import org.ovirt.engine.core.common.businessentities.Permission;
import org.ovirt.engine.core.common.businessentities.profiles.DiskProfile;
import org.ovirt.engine.core.common.businessentities.qos.StorageQos;
import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.uicommon.model.SearchableTableModelProvider;
import org.ovirt.engine.ui.common.widget.table.SimpleActionTable;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumn;
import org.ovirt.engine.ui.common.widget.uicommon.AbstractModelBoundTableWidget;
import org.ovirt.engine.ui.common.widget.uicommon.permissions.PermissionWithInheritedPermissionListModelTable;
import org.ovirt.engine.ui.uicommonweb.UICommand;
import org.ovirt.engine.ui.uicommonweb.models.configure.PermissionListModel;
import org.ovirt.engine.ui.uicommonweb.models.profiles.DiskProfileListModel;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.gin.AssetProvider;
import org.ovirt.engine.ui.webadmin.uicommon.model.DiskProfilePermissionModelProvider;
import org.ovirt.engine.ui.webadmin.widget.action.WebAdminButtonDefinition;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class DiskProfilesListModelTable extends AbstractModelBoundTableWidget<DiskProfile, DiskProfileListModel> {

    private static final String OBRAND_MAIN_TAB = "obrand_main_tab"; // $NON-NLS-1$

    interface WidgetUiBinder extends UiBinder<Widget, DiskProfilesListModelTable> {
        WidgetUiBinder uiBinder = GWT.create(WidgetUiBinder.class);
    }

    private final PermissionWithInheritedPermissionListModelTable<PermissionListModel<DiskProfile>> permissionListModelTable;

    @UiField
    FlowPanel tableContainer;

    private final DiskProfilePermissionModelProvider diskProfilePermissionModelProvider;

    private static final ApplicationConstants constants = AssetProvider.getConstants();

    public DiskProfilesListModelTable(SearchableTableModelProvider<DiskProfile, DiskProfileListModel> modelProvider,
            DiskProfilePermissionModelProvider diskProfilePermissionModelProvider,
            EventBus eventBus,
            ClientStorage clientStorage) {
        super(modelProvider, eventBus, clientStorage, false);
        this.diskProfilePermissionModelProvider = diskProfilePermissionModelProvider;
        getTable().removeStyleName(OBRAND_MAIN_TAB);
        // Create disk profile table
        tableContainer.add(getContainer());

        // Create permission panel
        permissionListModelTable =
                new PermissionWithInheritedPermissionListModelTable<>(diskProfilePermissionModelProvider,
                        eventBus,
                        clientStorage);
        permissionListModelTable.initTable();
        tableContainer.add(permissionListModelTable);
    }

    @Override
    protected Widget getWrappedWidget() {
        return WidgetUiBinder.uiBinder.createAndBindUi(this);
    }

    @Override
    public void initTable() {
        getTable().enableColumnResizing();

        AbstractTextColumn<DiskProfile> nameColumn =
                new AbstractTextColumn<DiskProfile>() {
                    @Override
                    public String getValue(DiskProfile object) {
                        return object.getName();
                    }
                };
        getTable().addColumn(nameColumn, constants.profileNameLabel(), "200px"); //$NON-NLS-1$
        nameColumn.makeSortable();

        AbstractTextColumn<DiskProfile> descriptionColumn =
                new AbstractTextColumn<DiskProfile>() {
                    @Override
                    public String getValue(DiskProfile object) {
                        return object.getDescription();
                    }
                };
        getTable().addColumn(descriptionColumn, constants.profileDescriptionLabel(), "200px"); //$NON-NLS-1$
        descriptionColumn.makeSortable();

        AbstractTextColumn<DiskProfile> qosColumn = new AbstractTextColumn<DiskProfile>() {
            @Override
            public String getValue(DiskProfile object) {
                String name = constants.unlimitedQos();
                if (object.getQosId() != null) {
                    StorageQos storageQos = getModel().getQos(object.getQosId());
                    if (storageQos != null) {
                        name = storageQos.getName();
                    }
                }
                return name;
            }
        };
        getTable().addColumn(qosColumn, constants.qosName(), "200px"); //$NON-NLS-1$
        qosColumn.makeSortable();

        addButtonToActionGroup(
        getTable().addActionButton(new WebAdminButtonDefinition<DiskProfile>(constants.newProfile()) {
            @Override
            protected UICommand resolveCommand() {
                return getModel().getNewCommand();
            }
        }));
        addButtonToActionGroup(
        getTable().addActionButton(new WebAdminButtonDefinition<DiskProfile>(constants.editProfile()) {
            @Override
            protected UICommand resolveCommand() {
                return getModel().getEditCommand();
            }
        }));
        addButtonToActionGroup(
        getTable().addActionButton(new WebAdminButtonDefinition<DiskProfile>(constants.removeProfile()) {
            @Override
            protected UICommand resolveCommand() {
                return getModel().getRemoveCommand();
            }
        }));

        // Add selection listener
        getModel().getSelectedItemChangedEvent().addListener((ev, sender, args) -> updatePermissionPanel());

        getModel().getItemsChangedEvent().addListener((ev, sender, args) -> updatePermissionPanel());
    }

    private void updatePermissionPanel() {
        final DiskProfile diskProfile = getModel().getSelectedItem();
        Scheduler.get().scheduleDeferred(() -> {
            if (permissionListModelTable.isVisible() && diskProfile == null) {
                permissionListModelTable.setVisible(false);
            } else if (!permissionListModelTable.isVisible() && diskProfile != null) {
                permissionListModelTable.setVisible(true);
            }
        });
    }

    @Override
    public void addModelListeners() {
        final SimpleActionTable<Permission> table = permissionListModelTable.getTable();
        table.getSelectionModel().addSelectionChangeHandler(event -> diskProfilePermissionModelProvider.setSelectedItems(table.getSelectedItems()));
    }
}
