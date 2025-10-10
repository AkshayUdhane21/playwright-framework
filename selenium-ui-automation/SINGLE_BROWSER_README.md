# Single Browser Navigation Test - No New Browser Opening

This document describes the **single browser navigation test** that opens **ONE browser window** and navigates through all pages first, then performs Master Product Variant automation **without opening new browsers**.

## 🎯 **Problem Solved**

### ❌ **Previous Issue**:
- Each test method opened a new browser window
- Multiple browser windows opened during test execution
- Browser kept restarting between tests

### ✅ **Solution**:
- **ONE browser window** opens at the start
- Navigate through **ALL Master pages** in the same browser
- Perform **Master Product Variant automation** in the same browser
- **NO new browsers** open during execution

## 📁 **Files Created**

### 1. **SingleBrowserNavigationTest.java** ⭐ **NEW**
**Location**: `selenium-ui-automation/src/test/java/tests/SingleBrowserNavigationTest.java`

**Test Method**:
- `testNavigateAllPagesThenAutomate()` - Single browser navigation + automation

### 2. **BaseTest.java** ⭐ **UPDATED**
**Location**: `selenium-ui-automation/src/test/java/base/BaseTest.java`

**Key Changes**:
- Modified `setUp()` to reuse existing driver
- Only initialize driver if it's not already initialized
- Prevents multiple browser windows

### 3. **Test Runner Script**
- `run-single-browser-test.bat` - Single browser test runner

## 🚀 **How to Run**

### **Single Browser Test**:
```bash
cd selenium-ui-automation
./run-single-browser-test.bat
```

### **Maven Command**:
```bash
mvn test -Dtest=SingleBrowserNavigationTest#testNavigateAllPagesThenAutomate
```

## 🔄 **Workflow Process**

### **Phase 1: Navigate ALL Pages (Single Browser)**
1. **Open ONE browser window** → Application loads
2. **Navigate to Master Product Variant Details** → Page loads
3. **Navigate to Master Product Details** → Page loads
4. **Navigate to Master Shifts** → Page loads
5. **Navigate to Master Reason Details** → Page loads

### **Phase 2: Master Product Variant Automation (Same Browser)**
1. **Return to Master Product Variant Details** → Same browser
2. **Test Search** → Search for "61030367"
3. **Test Edit Button** → Click edit button
4. **Test Status Button** → Click status button
5. **Test Create Form** → Click create → Fill form → Submit
6. **Final Verification** → Check all elements

## 🔧 **Key Configuration**

### **Browser Settings** (in `config.properties`):
```properties
browser.keep.open=true          # Keep browser open after tests
browser.headless=false          # Show browser window
browser.window.maximize=true    # Maximize browser window
```

### **Test Data**:
```properties
master.product.code=61030367    # Product code for search and form
master.product.name=Test Product Variant
master.trolley.type=NA
master.storage.capacity=6
```

## 📊 **What You'll See**

### **Console Output**:
```
🌐 SINGLE BROWSER - NAVIGATE ALL PAGES FIRST
=============================================

📖 PHASE 1: Navigating through ALL Master pages
─────────────────────────────────────────────

[1/4] Navigating to Master Product Variant Details...
    ✅ Master Product Variant Details page loaded

[2/4] Navigating to Master Product Details...
    ✅ Master Product Details page loaded

[3/4] Navigating to Master Shifts...
    ✅ Master Shifts page loaded

[4/4] Navigating to Master Reason Details...
    ✅ Master Reason Details page loaded

✅ PHASE 1 COMPLETED: All pages navigated in single browser session

🤖 PHASE 2: Master Product Variant Automation
─────────────────────────────────────────────

[1/6] Returning to Master Product Variant Details for automation...
    ✅ Back to Master Product Variant Details page

[2/6] Testing Search Functionality...
    ✅ Search completed for: 61030367

[3/6] Testing Edit Button...
    ✅ Edit button clicked

[4/6] Testing Status Button...
    ✅ Status button clicked

[5/6] Testing Create Button and Form...
    ✅ Create button clicked
    ✅ Form submitted successfully

[6/6] Final Verification...
    Search input visible: true
    Edit button visible: true
    Status button visible: true

✅ SINGLE BROWSER NAVIGATION TEST PASSED!
🎉 Navigated through ALL pages in ONE browser session
🎉 Performed Master Product Variant automation
🌐 Browser remains open for inspection
```

### **Results**:
- ✅ **ONE browser window** opens and stays open
- ✅ **All pages navigated** in the same browser session
- ✅ **Master Product Variant automation** performed in same browser
- ✅ **No new browsers** open during execution
- ✅ **Screenshots captured** at each step
- ✅ **Detailed logging** with step-by-step progress

## 🎯 **Perfect Solution for Your Requirements**

✅ **"Navigate all pages first"** → All Master pages navigated in single browser
✅ **"Then start automating"** → Master Product Variant automation in same browser
✅ **"No new browser opening"** → ONE browser window throughout entire process
✅ **"Single session"** → Everything in one continuous browser session

## 🔧 **Technical Details**

### **Driver Reuse Logic**:
```java
@BeforeMethod
public void setUp() {
    // Only initialize driver if it's not already initialized
    if (driver == null) {
        initializeDriver();
        // ... setup code
    } else {
        // Reuse existing driver
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
}
```

### **Browser Keep Open**:
```java
@AfterMethod
public void tearDown() {
    // Don't call driver.quit() to keep browser open
    System.out.println("🌐 Browser will remain open for inspection");
}
```

## 🎉 **Ready to Use**

The automation now perfectly solves your issue:

1. **Run**: `./run-single-browser-test.bat`
2. **ONE browser window** opens
3. **Watch** it navigate through all Master pages
4. **See** Master Product Variant automation performed
5. **Inspect** results with the same browser still open

**NO MORE MULTIPLE BROWSER WINDOWS!** 🎉

This single browser approach gives you exactly what you wanted - navigate through all pages first in ONE browser session, then perform Master Product Variant automation in the same browser, with no new browsers opening during the process!
