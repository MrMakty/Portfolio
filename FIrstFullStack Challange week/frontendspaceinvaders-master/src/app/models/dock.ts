export interface Dock {
  id: number;
  size: 'S' | 'M' | 'L';
  occupied: boolean;
  inMaintenance: boolean;
  startTimestamp?: Date;
  endTimestamp?: Date;
  level: number;
}
