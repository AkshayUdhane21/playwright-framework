# Master Product Variant Focused Automation

This document describes the **focused Master Product Variant automation** that navigates to the Master Product Variant Details page first, then performs all automation actions on that single page.

## 🎯 **Simple and Focused Approach**

### ✅ **Single Page Focus**
- Navigate to **Master Product Variant Details page ONLY**
- Perform all automation actions on that single page
- Keep browser open throughout the process

### ✅ **Complete Automation**
- Search functionality with code "61030367"
- Edit button testing
- Status button testing  
- Create form automation (click → fill → submit)
- All actions performed in sequence

## 📁 **Files Created**

### 1. **MasterProductVariantFocusedTest.java** ⭐ **NEW**
**Location**: `selenium-ui-automation/src/test/java/tests/MasterProductVariantFocusedTest.java`

**Test Methods**:
- `testMasterProductVariantCompleteAutomation()` - Complete automation on single page
- `testMasterProductVariantCreateFormOnly()` - Create form test only

### 2. **Test Runner Scripts**
- `run-master-product-variant-menu.bat` - Interactive menu with options
- `run-focused-automation.bat` - Complete automation test
- `run-create-form-test.bat` - Create form test only

## 🚀 **How to Run**

### **Option 1: Interactive Menu** ⭐ **RECOMMENDED**
```bash
cd selenium-ui-automation
./run-master-product-variant-menu.bat
```

**Menu Options**:
1. **Complete Automation** - Navigate + All Actions
2. **Create Form Test Only** - Create form functionality only
3. **Run Both Tests** - Execute both tests
4. **Exit**

### **Option 2: Direct Commands**
```bash
# Complete automation
./run-focused-automation.bat

# Create form test only
./run-create-form-test.bat
```

### **Option 3: Maven Commands**
```bash
# Complete automation
mvn test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCompleteAutomation

# Create form test only
mvn test -Dtest=MasterProductVariantFocusedTest#testMasterProductVariantCreateFormOnly

# Both tests
mvn test -Dtest=MasterProductVariantFocusedTest
```

## 🔄 **Workflow Process**

### **Complete Automation Test**:

#### **Step 1: Navigation**
1. **Click Master Button** → Opens dropdown menu
2. **Click Master Product Variant Details** → Navigates to page
3. **Initialize Page Object** → Ready for automation

#### **Step 2: Automation Actions**
1. **Test Search** → Search for "61030367"
2. **Test Edit Button** → Click edit button
3. **Test Status Button** → Click status button
4. **Test Create Form** → Click create → Fill form → Submit
5. **Test Search Again** → Verify search still works
6. **Final Verification** → Check all elements visible

### **Create Form Test Only**:
1. **Navigate to Master Product Variant Details** → Direct navigation
2. **Test Create Form** → Complete form automation
3. **Capture Screenshot** → Document results

## 🔧 **Configuration**

### **Test Data** (in `config.properties`):
```properties
master.product.code=61030367    # Product code for search and form
master.product.name=Test Product Variant
master.trolley.type=NA
master.storage.capacity=6
browser.keep.open=true          # Keep browser open after tests
```

## 📊 **What You'll See**

### **Console Output**:
```
🎯 MASTER PRODUCT VARIANT COMPLETE AUTOMATION
=============================================

[1/7] Navigating to Master Product Variant Details page...
   🔄 Clicking Master button to open dropdown...
   ✅ Master dropdown opened
   🔄 Clicking Master Product Variant Details...
   ✅ Master Product Variant Details page loaded

[2/7] Testing Search Functionality...
   🔍 Searching for product code: 61030367
   ✅ Search completed successfully

[3/7] Testing Edit Button...
   🔄 Clicking edit button...
   ✅ Edit button clicked successfully

[4/7] Testing Status Button...
   🔄 Clicking status button...
   ✅ Status button clicked successfully

[5/7] Testing Create Button and Form...
   🔄 Clicking create button...
   ✅ Create button clicked successfully
   📝 Filling create form...
      Product Code: 61030367
      Product Name: Test Product Variant
      Trolley Type: NA
      Storage Capacity: 6
   ✅ Form filled successfully
   🔄 Submitting form...
   ✅ Form submitted successfully

[6/7] Testing Search Again...
   🔍 Searching again for: 61030367
   ✅ Search completed again successfully

[7/7] Final Verification...
   📊 Element Visibility Check:
      Search input visible: true
      Edit button visible: true
      Status button visible: true

✅ MASTER PRODUCT VARIANT AUTOMATION COMPLETED!
🌐 Browser remains open for inspection
📸 Screenshots captured at each step
```

### **Results**:
- ✅ **Browser stays open** for inspection
- ✅ **Screenshots captured** at each step
- ✅ **Detailed console output** with progress
- ✅ **Extent reports** with comprehensive logging
- ✅ **All actions performed** on single page

## 🎯 **Perfect Match for Your Requirements**

✅ **"Navigate to Master Product Variant Details page first"** → Single page navigation
✅ **"Then start automating"** → Complete automation on that page
✅ **"Keep browser open"** → Browser remains open throughout
✅ **"Create form and add input"** → Complete form automation
✅ **"Single workflow"** → Everything in one focused test

## 🎉 **Ready to Use**

The automation is now ready and perfectly matches your requirements:

1. **Run the menu**: `./run-master-product-variant-menu.bat`
2. **Choose option 1** for complete automation
3. **Watch the browser** navigate to Master Product Variant Details page
4. **See all automation actions** performed on that single page
5. **Inspect the results** with browser still open

This focused approach gives you exactly what you wanted - a simple workflow that navigates to the Master Product Variant Details page first, then performs all automation actions on that single page, keeping the browser open throughout the entire process!
