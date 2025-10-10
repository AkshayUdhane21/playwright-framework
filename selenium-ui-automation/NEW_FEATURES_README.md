# New Automation Features - Edit, Status, Search & Navigation Flow

This document describes the newly added automation features for the Master Product Variant page, including **proper navigation flow**, edit button, status button, and search functionality.

## 🆕 New Features Added

### 1. **Navigation Flow Enhancement** ⭐ **NEW**
- **Master Button**: `//*[@id="root"]/div/header/nav/div[1]`
- **Dropdown Menu Items**:
  - Master Product Variant Details: `//*[@id="root"]/div/header/nav/div[1]/div/a[1]`
  - Master Product Details: `//*[@id="root"]/div/header/nav/div[1]/div/a[2]`
  - Master Shifts: `//*[@id="root"]/div/header/nav/div[1]/div/a[3]`
  - Master Reason Details: `//*[@id="root"]/div/header/nav/div[1]/div/a[4]`
- **Functionality**: Proper navigation flow using dropdown menu
- **Test Method**: `testNavigationFlowToMasterProductVariantDetails()`

### 2. Edit Button Automation
- **XPath**: `//*[@id="root"]/div/main/div/div[3]/table/tbody/tr[1]/td[6]/button[1]`
- **Functionality**: Clicks the edit button for the first row in the table
- **Test Method**: `testEditButtonFunctionality()`

### 3. Status Button Automation  
- **XPath**: `//*[@id="root"]/div/main/div/div[3]/table/tbody/tr[1]/td[6]/button[3]`
- **Functionality**: Clicks the status button for the first row in the table
- **Test Method**: `testStatusButtonFunctionality()`

### 4. Search Functionality
- **XPath**: `//*[@id="root"]/div/main/div/div[2]/div[2]/input`
- **Functionality**: Searches for product variant using code "61030367"
- **Test Method**: `testSearchFunctionality()`

### 5. Complete Workflow Test
- **Test Method**: `testCompleteWorkflowWithAllActions()`
- **Functionality**: Tests all actions in sequence (navigation → search → edit → status → create)

## 📁 Files Modified

### 1. **HomePage.java** ⭐ **UPDATED**
**Location**: `selenium-ui-automation/src/test/java/pages/HomePage.java`

**New Methods Added**:
- `clickMasterProductDetails()` - Clicks Master Product Details link
- `clickMasterShifts()` - Clicks Master Shifts link  
- `clickMasterReasonDetails()` - Clicks Master Reason Details link
- `navigateToMasterProductVariantDetailsWithFlow()` - **NEW** - Proper navigation flow
- `isMasterProductDetailsLinkVisible()` - Checks Master Product Details visibility
- `isMasterShiftsLinkVisible()` - Checks Master Shifts visibility
- `isMasterReasonDetailsLinkVisible()` - Checks Master Reason Details visibility
- `isDropdownMenuOpen()` - Checks if dropdown menu is open

**New Locators Added**:
```java
private static final By MASTER_PRODUCT_DETAILS_LINK = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]/div/a[2]");
private static final By MASTER_SHIFTS_LINK = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]/div/a[3]");
private static final By MASTER_REASON_DETAILS_LINK = By.xpath("//*[@id=\"root\"]/div/header/nav/div[1]/div/a[4]");
```

### 2. MasterProductVariantPage.java
**Location**: `selenium-ui-automation/src/test/java/pages/MasterProductVariantPage.java`

**New Methods Added**:
- `clickEditButton()` - Clicks edit button with multiple fallback strategies
- `clickStatusButton()` - Clicks status button with multiple fallback strategies  
- `searchProductVariant(String searchCode)` - Searches for product variant
- `clearSearch()` - Clears search input
- `isEditButtonVisible()` - Checks if edit button is visible
- `isStatusButtonVisible()` - Checks if status button is visible
- `isSearchInputVisible()` - Checks if search input is visible
- `getSearchValue()` - Gets current search input value

**New Locators Added**:
```java
private static final By EDIT_BUTTON = By.xpath("//*[@id=\"root\"]/div/main/div/div[3]/table/tbody/tr[1]/td[6]/button[1]");
private static final By STATUS_BUTTON = By.xpath("//*[@id=\"root\"]/div/main/div/div[3]/table/tbody/tr[1]/td[6]/button[3]");
private static final By SEARCH_INPUT = By.xpath("//*[@id=\"root\"]/div/main/div/div[2]/div[2]/input");
```

### 2. SeleniumUtils.java
**Location**: `selenium-ui-automation/src/test/java/utils/SeleniumUtils.java`

**New Methods Added**:
- `pressEnterKey(WebDriver driver, By locator)` - Presses Enter key on element
- `pressTabKey(WebDriver driver, By locator)` - Presses Tab key on element
- `pressEscapeKey(WebDriver driver, By locator)` - Presses Escape key on element

### 3. MasterProductVariantTest.java
**Location**: `selenium-ui-automation/src/test/java/tests/MasterProductVariantTest.java`

**New Test Methods Added**:
- `testNavigationFlowToMasterProductVariantDetails()` (Priority 11) ⭐ **NEW**
- `testEditButtonFunctionality()` (Priority 7)
- `testStatusButtonFunctionality()` (Priority 8)  
- `testSearchFunctionality()` (Priority 9)
- `testCompleteWorkflowWithAllActions()` (Priority 10)

**Updated Test Methods**:
- All existing test methods now use the new navigation flow
- Navigation flow is tested separately for better debugging

## 🚀 How to Run the New Tests

### Option 1: Run All New Tests
```bash
cd selenium-ui-automation
./run-new-tests.bat
```

### Option 2: Run Individual Tests
```bash
# Navigation Flow Test (NEW)
mvn test -Dtest=MasterProductVariantTest#testNavigationFlowToMasterProductVariantDetails

# Edit Button Test
mvn test -Dtest=MasterProductVariantTest#testEditButtonFunctionality

# Status Button Test  
mvn test -Dtest=MasterProductVariantTest#testStatusButtonFunctionality

# Search Functionality Test
mvn test -Dtest=MasterProductVariantTest#testSearchFunctionality

# Complete Workflow Test
mvn test -Dtest=MasterProductVariantTest#testCompleteWorkflowWithAllActions
```

### Option 3: Run Navigation Flow Test Only
```bash
cd selenium-ui-automation
./run-navigation-test.bat
```

### Option 4: Run All Tests (Including Existing)
```bash
mvn test -Dtest=MasterProductVariantTest
```

## 🔧 Test Configuration

The tests use the following configuration:
- **Navigation Flow**: Proper dropdown menu navigation (Master → Master Product Variant Details)
- **Search Code**: `61030367` (as specified in requirements)
- **Thread Sleep**: 1000ms between actions for stability
- **Screenshots**: Automatically captured for each test step
- **Extent Reports**: Detailed reporting for each test
- **Dropdown XPaths**: All dropdown menu items properly mapped

## 📊 Test Results

After running the tests, check the following locations:
- **Console Output**: Real-time test execution logs
- **Screenshots**: `selenium-ui-automation/screenshots/` folder
- **Test Reports**: `selenium-ui-automation/target/surefire-reports/`
- **HTML Reports**: `selenium-ui-automation/test-output/`

## 🛠️ Troubleshooting

### Common Issues:

1. **Elements Not Found**: 
   - Tests include fallback strategies for element location
   - Multiple XPath strategies are used for each element

2. **Timing Issues**:
   - Tests include appropriate wait times
   - Thread.sleep() is used strategically for stability

3. **No Data in Table**:
   - Tests handle cases where edit/status buttons might not be visible
   - Informational messages are logged instead of failures

### Debug Mode:
To run tests with more verbose output:
```bash
mvn test -Dtest=MasterProductVariantTest -Dmaven.surefire.debug
```

## 📝 Test Flow

### **Navigation Flow Test** ⭐ **NEW**:
1. Verify Master button is visible
2. Click Master button to open dropdown
3. Verify dropdown menu items are visible
4. Navigate to Master Product Variant Details using proper flow
5. Verify page elements are visible

### Complete Workflow Test Flow:
1. Navigate to Master Product Variant Details page using dropdown flow
2. Test search functionality with code "61030367"
3. Test edit button functionality
4. Test status button functionality  
5. Test create button functionality (existing)
6. Final verification of all elements

### Individual Test Flows:
Each individual test follows this pattern:
1. Navigate to the page using dropdown flow
2. Verify element visibility
3. Perform the action (click/search)
4. Capture screenshot
5. Log results

## ✅ Success Criteria

Tests are considered successful if:
- **Navigation flow works properly** (Master button → dropdown → Master Product Variant Details)
- Elements are found and interacted with
- Screenshots are captured
- No critical exceptions occur
- Extent reports are generated

**Note**: Some tests may show informational messages instead of failures if elements are not visible (e.g., when no data exists in the table), which is expected behavior.

## 🎯 **Key Improvement: Proper Navigation Flow**

The main enhancement in this update is the **proper navigation flow** that follows the exact user journey:

1. **Click Master button** → Opens dropdown menu
2. **Wait for dropdown** → Ensures menu is visible
3. **Click Master Product Variant Details** → Navigates to the correct page
4. **Verify page elements** → Confirms successful navigation

This ensures the automation matches the real user experience and handles the dropdown menu interaction properly.
