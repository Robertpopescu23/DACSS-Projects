# Fundamentals Architectural Styles Projects

This branch contains multiple implementations of fundamental architectural styles for the DACSS course assignments.

---

## 1. Pipes-and-Filters

**Overview:**  
A system where data flows through a sequence of filters, each performing a transformation or processing step. Components are organized linearly and the assembler ensures the correct order.

**Implementation Highlights:**
- Filters are reusable components that process messages (reviews) in sequence.
- Tasks include eliminating invalid messages, filtering profanities and political content, resizing images, removing competitor links, and sentiment analysis.
- Concurrent version: Filters can run in parallel, synchronized properly to avoid race conditions.

**Example:**
Input:
John, Laptop, ok, PICTURE
Mary, Phone, @#$%), IMAGE
Peter, Phone, GREAT, ManyPictures
Ann, Book, So GOOD, Image

Output:
John, Laptop, ok-, picture
Ann, Book, So GOOD+, image

---

## 2. Blackboard

**Overview:**  
A system where components (knowledge sources) interact through a shared blackboard, reading and writing messages independently. Each component decides when to act based on current blackboard state.

**Implementation Highlights:**
- Eliminator and transformer components access the blackboard asynchronously.
- Tasks are performed according to logical dependencies: elimination tasks first, then transformation.
- Concurrent version: Knowledge sources are active and synchronized to safely update the blackboard.

---

## 3. Event-Driven

**Overview:**  
Two applications demonstrating event-driven architecture using a **custom BasicEventBus**:

1. **Sensor Monitoring System**  
   - Multiple sensor types generate simulated data events.  
   - Displays subscribe to specific sensor types and react to events automatically.

2. **News Agencies and Subscribers**  
   - Agencies publish news in multiple domains.  
   - Subscribers register interest in domains and receive notifications of new events.

**Implementation Highlights:**
- Publisher–subscriber model decouples senders and receivers.  
- EventBus handles event distribution within the process.  
- Supports multiple event types defined per application.

---

## Notes

- Each project is implemented in a separate subdirectory:  
  - `pipes-and-filters/`  
  - `blackboard/`  
  - `event-driven/`
- The branch **Fundamentals-Architectural-Styles** contains only these projects.  
- Main branch contains only the general repository README.
