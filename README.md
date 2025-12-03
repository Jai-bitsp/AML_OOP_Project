# AML Graph Engine (MVP)

A simplified, class‑friendly MVP that demonstrates real‑time graph modeling of transactions and a single core detector for suspicious rapid ping‑pong transfers. It keeps the overall idea (streaming updates, alerting, and visualization) while reducing moving parts and dependencies.

## Highlights
- Real-time dynamic graph updated per transaction event.
- Single detector: rapid ping‑pong transfers within a time window.
- JavaFX UI with live GraphStream visualization, alert feed, controls, and stats.
- Synthetic simulator data source (MVP).

## Tech Stack
- `Java 17+`
- Build: `Maven`
- UI: `Swing` (no external UI dependencies)

## Architecture
- Core engine (`AccountNode`, `TransactionEdge`, `DynamicGraph`).
- Observer interface (`GraphObserver`) and detector (`RapidTransactionDetector`).
- Stream (`TransactionStreamSimulator`).
- UI (`GraphView` Swing panel, `AlertsPanel` Swing panel, `ControlsPane`, `StatsPane`).

See detailed diagrams and colors in [`architecture.md`](architecture.md).

## Data Source
- Synthetic generator (offline, no external dependencies).

## How It Works
- Each transaction updates the `DynamicGraph` and notifies the rapid‑transfer detector.
- The detector counts recent A→B and B→A edges within a window and emits alerts when both exceed a threshold.
- Alerts raise risk scores for involved accounts and are shown in the UI.

## UI Overview
- Live graph visualization (Swing paint) with node color mapped to risk (green → red) and edge thickness mapped to amount.
- Alert feed with detector type and details.
- Controls to start/stop the simulator and toggle the detector.
- Stats with node and edge counts.

## Project Structure
```
src/main/java/
  engine/
    AccountNode.java
    TransactionEdge.java
    DynamicGraph.java
  observers/
    GraphObserver.java
    RapidTransactionDetector.java
  stream/
    TransactionStreamSimulator.java
  ui/
    App.java
    GraphView.java
    AlertsPanel.java
    ControlsPane.java
    StatsPane.java
resources/
```

## Build & Run
- Requirements: Java 17+, Maven.
- Build: `mvn -q -DskipTests package`
- Run: `java -cp target/classes com.oops.aml.ui.App`

## Roadmap
- [ ] Optional: add more detectors (cycles, layering).
- [ ] Optional: CSV player for datasets.
- [ ] Optional: thresholds configured via UI.

## Disclaimer
This project is for educational and research exploration of real-time graph analytics patterns in AML. It is not a production AML system and should not be used for compliance decisions.
