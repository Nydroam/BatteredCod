var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
/* Generated from Java with JSweet 2.0.0-SNAPSHOT - http://www.jsweet.org */
var bc19;
(function (bc19) {
    var Action = (function () {
        function Action(signal, signalRadius, logs, castleTalk) {
            this.signal = 0;
            this.signal_radius = 0;
            this.logs = null;
            this.castle_talk = 0;
            this.signal = signal;
            this.signal_radius = signalRadius;
            this.logs = logs;
            this.castle_talk = castleTalk;
        }
        return Action;
    }());
    bc19.Action = Action;
    Action["__class"] = "bc19.Action";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Logistics = (function () {
        function Logistics() {
        }
        Logistics.findOpposite = function (r, x, y, s) {
            var yMax = r.map.length - 1;
            var xMax = r.map[0].length - 1;
            var coors = ([]);
            if (s === Logistics.ALL || s === Logistics.VERTICAL) {
                var coor = new Array(2);
                coor[0] = y;
                coor[1] = xMax - x;
                /* add */ (coors.push(coor) > 0);
            }
            if (s === Logistics.ALL || s === Logistics.HORIZONTAL) {
                var coor = new Array(2);
                coor[0] = yMax - y;
                coor[1] = x;
                /* add */ (coors.push(coor) > 0);
            }
            if (s === Logistics.ALL || s === Logistics.DIAGONAL) {
                var coor = new Array(2);
                coor[0] = yMax - y;
                coor[1] = xMax - x;
                /* add */ (coors.push(coor) > 0);
            }
            return coors;
        };
        Logistics.symmetry = function (map, r) {
            var startTime = Date.now();
            Logistics.rob = r;
            var halfMap = ([]);
            var v = true;
            var h = true;
            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {
                    /* add */ (halfMap.push(map[y][x]) > 0);
                }
                ;
                if (!Logistics.checkSym(halfMap)) {
                    v = false;
                    break;
                }
                halfMap = ([]);
            }
            ;
            halfMap = ([]);
            for (var x = 0; x < map[0].length; x++) {
                for (var y = 0; y < map.length; y++) {
                    /* add */ (halfMap.push(map[y][x]) > 0);
                }
                ;
                if (!Logistics.checkSym(halfMap)) {
                    h = false;
                    break;
                }
                halfMap = ([]);
            }
            ;
            var diff = Date.now() - startTime;
            r.log("Symmetry Time: " + diff + " ms");
            if (v && h) {
                return Logistics.ALL;
            }
            else if (v) {
                return Logistics.VERTICAL;
            }
            else if (h) {
                return Logistics.HORIZONTAL;
            }
            else {
                return Logistics.DIAGONAL;
            }
        };
        Logistics.checkSym = function (hM) {
            while ((hM.length > 1)) {
                if (!(function (o1, o2) { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(/* poll */ (function (a) { return a.length == 0 ? null : a.shift(); })(hM), /* pop */ hM.pop())) {
                    return false;
                }
            }
            ;
            return true;
        };
        Logistics.printBoolMap = function (r, map) {
            for (var i = 0; i < map.length; i++) {
                var line = "";
                for (var j = 0; j < map[0].length; j++) {
                    if (map[i][j])
                        line += "T";
                    else
                        line += "-";
                    line += " ";
                }
                ;
                r.log(line);
            }
            ;
            r.log("");
        };
        return Logistics;
    }());
    Logistics.VERTICAL = 0;
    Logistics.HORIZONTAL = 1;
    Logistics.DIAGONAL = 2;
    Logistics.ALL = 3;
    Logistics.rob = null;
    bc19.Logistics = Logistics;
    Logistics["__class"] = "bc19.Logistics";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Resource = (function () {
        function Resource(x, y, p) {
            this.x = 0;
            this.y = 0;
            this.worker = 0;
            this.priority = 0;
            this.isKarb = false;
            this.replaced = false;
            this.x = x;
            this.y = y;
            this.priority = p;
            this.worker = -1;
        }
        return Resource;
    }());
    bc19.Resource = Resource;
    Resource["__class"] = "bc19.Resource";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var BCException = (function (_super) {
        __extends(BCException, _super);
        function BCException(errorMessage) {
            var _this = _super.call(this, errorMessage) || this;
            _this.message = errorMessage;
            Object.setPrototypeOf(_this, BCException.prototype);
            return _this;
        }
        return BCException;
    }(Error));
    bc19.BCException = BCException;
    BCException["__class"] = "bc19.BCException";
    BCException["__interfaces"] = ["java.io.Serializable"];
})(bc19 || (bc19 = {}));
(function (bc19) {
    var BCAbstractRobot = (function () {
        function BCAbstractRobot() {
            this.SPECS = null;
            this.gameState = null;
            this.logs = null;
            this.__signal = 0;
            this.signalRadius = 0;
            this.__castleTalk = 0;
            this.me = null;
            this.id = 0;
            this.fuel = 0;
            this.karbonite = 0;
            this.lastOffer = null;
            this.map = null;
            this.karboniteMap = null;
            this.fuelMap = null;
            this.resetState();
        }
        BCAbstractRobot.prototype.setSpecs = function (specs) {
            this.SPECS = specs;
        };
        /*private*/ BCAbstractRobot.prototype.resetState = function () {
            this.logs = ([]);
            this.__signal = 0;
            this.signalRadius = 0;
            this.__castleTalk = 0;
        };
        BCAbstractRobot.prototype._do_turn = function (gameState) {
            this.gameState = gameState;
            this.id = gameState.id;
            this.karbonite = gameState.karbonite;
            this.fuel = gameState.fuel;
            this.lastOffer = gameState.last_offer;
            this.me = this.getRobot(this.id);
            if (this.me.turn === 1) {
                this.map = gameState.map;
                this.karboniteMap = gameState.karbonite_map;
                this.fuelMap = gameState.fuel_map;
            }
            var t = null;
            try {
                t = this.turn();
            }
            catch (e) {
                t = new bc19.ErrorAction(e, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
            }
            ;
            if (t == null)
                t = new bc19.Action(this.__signal, this.signalRadius, this.logs, this.__castleTalk);
            t.signal = this.__signal;
            t.signal_radius = this.signalRadius;
            t.logs = this.logs;
            t.castle_talk = this.__castleTalk;
            this.resetState();
            return t;
        };
        /*private*/ BCAbstractRobot.prototype.checkOnMap = function (x, y) {
            return x >= 0 && x < this.gameState.shadow[0].length && y >= 0 && y < this.gameState.shadow.length;
        };
        BCAbstractRobot.prototype.log = function (message) {
            /* add */ (this.logs.push(message) > 0);
        };
        BCAbstractRobot.prototype.signal = function (value, radius) {
            var fuelNeeded = (Math.ceil(Math.sqrt(radius)) | 0);
            if (this.fuel < fuelNeeded)
                throw new bc19.BCException("Not enough fuel to signal given radius.");
            if (value < 0 || value >= Math.pow(2, this.SPECS.COMMUNICATION_BITS))
                throw new bc19.BCException("Invalid signal, must be within bit range.");
            if (radius > 2 * Math.pow(this.SPECS.MAX_BOARD_SIZE - 1, 2))
                throw new bc19.BCException("Signal radius is too big.");
            this.__signal = value;
            this.signalRadius = radius;
            this.fuel -= fuelNeeded;
        };
        BCAbstractRobot.prototype.castleTalk = function (value) {
            if (value < 0 || value >= Math.pow(2, this.SPECS.CASTLE_TALK_BITS))
                throw new bc19.BCException("Invalid castle talk, must be between 0 and 2^8.");
            this.__castleTalk = value;
        };
        BCAbstractRobot.prototype.proposeTrade = function (k, f) {
            if (this.me.unit !== this.SPECS.CASTLE)
                throw new bc19.BCException("Only castles can trade.");
            if (Math.abs(k) >= this.SPECS.MAX_TRADE || Math.abs(f) >= this.SPECS.MAX_TRADE)
                throw new bc19.BCException("Cannot trade over " + ('' + (this.SPECS.MAX_TRADE)) + " in a given turn.");
            return new bc19.TradeAction(f, k, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.buildUnit = function (unit, dx, dy) {
            if (this.me.unit !== this.SPECS.PILGRIM && this.me.unit !== this.SPECS.CASTLE && this.me.unit !== this.SPECS.CHURCH)
                throw new bc19.BCException("This unit type cannot build.");
            if (this.me.unit === this.SPECS.PILGRIM && unit !== this.SPECS.CHURCH)
                throw new bc19.BCException("Pilgrims can only build churches.");
            if (this.me.unit !== this.SPECS.PILGRIM && unit === this.SPECS.CHURCH)
                throw new bc19.BCException("Only pilgrims can build churches.");
            if (dx < -1 || dy < -1 || dx > 1 || dy > 1)
                throw new bc19.BCException("Can only build in adjacent squares.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t build units off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] !== 0)
                throw new bc19.BCException("Cannot build on occupied tile.");
            if (!this.map[this.me.y + dy][this.me.x + dx])
                throw new bc19.BCException("Cannot build onto impassable terrain.");
            if (this.karbonite < this.SPECS.UNITS[unit].CONSTRUCTION_KARBONITE || this.fuel < this.SPECS.UNITS[unit].CONSTRUCTION_FUEL)
                throw new bc19.BCException("Cannot afford to build specified unit.");
            return new bc19.BuildAction(unit, dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.move = function (dx, dy) {
            if (this.me.unit === this.SPECS.CASTLE || this.me.unit === this.SPECS.CHURCH)
                throw new bc19.BCException("Churches and Castles cannot move.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t move off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] === -1)
                throw new bc19.BCException("Cannot move outside of vision range.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] !== 0)
                throw new bc19.BCException("Cannot move onto occupied tile.");
            if (!this.map[this.me.y + dy][this.me.x + dx])
                throw new bc19.BCException("Cannot move onto impassable terrain.");
            var r = dx * dx + dy * dy;
            if (r > this.SPECS.UNITS[this.me.unit].SPEED)
                throw new bc19.BCException("Slow down, cowboy.  Tried to move faster than unit can.");
            if (this.fuel < r * this.SPECS.UNITS[this.me.unit].FUEL_PER_MOVE)
                throw new bc19.BCException("Not enough fuel to move at given speed.");
            return new bc19.MoveAction(dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.mine = function () {
            if (this.me.unit !== this.SPECS.PILGRIM)
                throw new bc19.BCException("Only Pilgrims can mine.");
            if (this.fuel < this.SPECS.MINE_FUEL_COST)
                throw new bc19.BCException("Not enough fuel to mine.");
            if (this.karboniteMap[this.me.y][this.me.x]) {
                if (this.me.karbonite >= this.SPECS.UNITS[this.SPECS.PILGRIM].KARBONITE_CAPACITY)
                    throw new bc19.BCException("Cannot mine, as at karbonite capacity.");
            }
            else if (this.fuelMap[this.me.y][this.me.x]) {
                if (this.me.fuel >= this.SPECS.UNITS[this.SPECS.PILGRIM].FUEL_CAPACITY)
                    throw new bc19.BCException("Cannot mine, as at fuel capacity.");
            }
            else
                throw new bc19.BCException("Cannot mine square without fuel or karbonite.");
            return new bc19.MineAction(this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.give = function (dx, dy, k, f) {
            if (dx > 1 || dx < -1 || dy > 1 || dy < -1 || (dx === 0 && dy === 0))
                throw new bc19.BCException("Can only give to adjacent squares.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t give off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] <= 0)
                throw new bc19.BCException("Cannot give to empty square.");
            if (k < 0 || f < 0 || this.me.karbonite < k || this.me.fuel < f)
                throw new bc19.BCException("Do not have specified amount to give.");
            return new bc19.GiveAction(k, f, dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.attack = function (dx, dy) {
            if (this.me.unit === this.SPECS.CHURCH)
                throw new bc19.BCException("Churches cannot attack.");
            if (this.fuel < this.SPECS.UNITS[this.me.unit].ATTACK_FUEL_COST)
                throw new bc19.BCException("Not enough fuel to attack.");
            if (!this.checkOnMap(this.me.x + dx, this.me.y + dy))
                throw new bc19.BCException("Can\'t attack off of map.");
            if (this.gameState.shadow[this.me.y + dy][this.me.x + dx] === -1)
                throw new bc19.BCException("Cannot attack outside of vision range.");
            var r = dx * dx + dy * dy;
            if (r > this.SPECS.UNITS[this.me.unit].ATTACK_RADIUS[1] || r < this.SPECS.UNITS[this.me.unit].ATTACK_RADIUS[0])
                throw new bc19.BCException("Cannot attack outside of attack range.");
            return new bc19.AttackAction(dx, dy, this.__signal, this.signalRadius, this.logs, this.__castleTalk);
        };
        BCAbstractRobot.prototype.getRobot = function (id) {
            if (id <= 0)
                return null;
            for (var i = 0; i < this.gameState.visible.length; i++) {
                if (this.gameState.visible[i].id === id) {
                    return this.gameState.visible[i];
                }
            }
            ;
            return null;
        };
        BCAbstractRobot.prototype.isVisible = function (robot) {
            for (var x = 0; x < this.gameState.shadow[0].length; x++) {
                for (var y = 0; y < this.gameState.shadow.length; y++) {
                    if (robot.id === this.gameState.shadow[y][x])
                        return true;
                }
                ;
            }
            ;
            return false;
        };
        BCAbstractRobot.prototype.isRadioing = function (robot) {
            return robot.signal >= 0;
        };
        BCAbstractRobot.prototype.getVisibleRobotMap = function () {
            return this.gameState.shadow;
        };
        BCAbstractRobot.prototype.getPassableMap = function () {
            return this.map;
        };
        BCAbstractRobot.prototype.getKarboniteMap = function () {
            return this.karboniteMap;
        };
        BCAbstractRobot.prototype.getFuelMap = function () {
            return this.fuelMap;
        };
        BCAbstractRobot.prototype.getVisibleRobots = function () {
            return this.gameState.visible;
        };
        BCAbstractRobot.prototype.turn = function () {
            return null;
        };
        return BCAbstractRobot;
    }());
    bc19.BCAbstractRobot = BCAbstractRobot;
    BCAbstractRobot["__class"] = "bc19.BCAbstractRobot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Bot = (function () {
        function Bot(r) {
            this.r = null;
            this.me = null;
            this.blockers = null;
            this.lblockers = null;
            this.fullyInit = false;
            this.numCastles = 0;
            this.enemyCastles = null;
            this.myCastles = null;
            this.symmetry = 0;
            this.currTarget = 0;
            this.even = false;
            this.currTarget = -1;
            this.update(r);
        }
        Bot.prototype.update = function (newr) {
            this.r = newr;
            this.me = this.r.me;
            this.blockers = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([newr.map.length, newr.map[0].length]);
            {
                var array122 = this.r.getVisibleRobots();
                for (var index121 = 0; index121 < array122.length; index121++) {
                    var other = array122[index121];
                    {
                        if (this.r.isVisible(other) && other.id !== this.me.id) {
                            this.blockers[other.y][other.x] = true;
                        }
                    }
                }
            }
            this.lblockers = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([newr.map.length, newr.map[0].length]);
            {
                var array124 = this.r.getVisibleRobots();
                for (var index123 = 0; index123 < array124.length; index123++) {
                    var other = array124[index123];
                    {
                        if (this.r.isVisible(other) && other.id !== this.me.id && other.team === this.me.team) {
                            this.lblockers[other.y][other.x] = true;
                        }
                    }
                }
            }
        };
        Bot.prototype.act = function () {
            return null;
        };
        Bot.prototype.attack = function () {
            var target = null;
            if (this.currTarget === -1 || (function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(this.r.getRobot(this.currTarget), null)) {
                var min = 9999;
                {
                    var array126 = this.r.getVisibleRobots();
                    for (var index125 = 0; index125 < array126.length; index125++) {
                        var other = array126[index125];
                        {
                            if (this.r.isVisible(other) && other.team !== this.me.team) {
                                var dist = bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y);
                                var range = 16;
                                if (this.me.unit === 4 || this.me.unit === 0)
                                    range = 64;
                                if (dist <= range) {
                                    if (target == null || target.unit === 2 || other.unit !== 2) {
                                        if (dist < min && !(this.me.unit === 4 && dist < 16)) {
                                            target = other;
                                            min = dist;
                                            this.currTarget = other.id;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
                target = this.r.getRobot(this.currTarget);
            if ((function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(target, null))
                return null;
            return this.r.attack(target.x - this.me.x, target.y - this.me.y);
        };
        return Bot;
    }());
    bc19.Bot = Bot;
    Bot["__class"] = "bc19.Bot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Pathing = (function () {
        function Pathing() {
        }
        Pathing.distance = function (x1, y1, x2, y2) {
            return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        };
        Pathing.printMap = function (map, r) {
            for (var y = 0; y < map.length; y++) {
                var line = "";
                for (var x = 0; x < map[y].length; x++) {
                    if (map[y][x] === 99)
                        line += "  |";
                    else {
                        var i = map[y][x];
                        var count = 0;
                        while ((i > 0)) {
                            i = (Math.floor((i / 10 | 0)) | 0);
                            count++;
                        }
                        ;
                        if (map[y][x] !== 0) {
                            for (var j = 0; j < 2 - count; j++)
                                line += "0";
                            line += map[y][x];
                        }
                        else {
                            line += "--";
                        }
                        line += "|";
                    }
                }
                ;
                r.log(line);
            }
            ;
        };
        Pathing.findRange = function (r, startX, startY, range, blockers, dirMap) {
            var steps = (Math.floor(Math.sqrt(range)) | 0);
            var results = ([]);
            var yMin = new Array(steps * 2 + 1);
            var yMax = new Array(steps * 2 + 1);
            var prevCoor = ([]);
            for (var i = 0; i < steps * 2 + 1; i++)
                (prevCoor.push(null) > 0);
            for (var y = 0 - steps; y <= steps; y++) {
                var minX = false;
                var maxX = false;
                for (var x = 0 - steps; x <= steps; x++) {
                    if (x === 0 && y === 0) {
                        continue;
                    }
                    var dist = Pathing.distance(0, 0, x, y);
                    if (dist <= range && Pathing.checkBounds(r, x + startX, y + startY, blockers)) {
                        if (dirMap[y + startY][x + startX] === 99) {
                            var coor = new Array(4);
                            coor[0] = y + startY;
                            coor[1] = x + startX;
                            if (!minX) {
                                minX = true;
                                coor[2] = 1;
                            }
                            else if (!maxX) {
                                maxX = true;
                                coor[2] = 1;
                            }
                            else {
                                /* get */ results[results.length - 1][2] = 0;
                                coor[2] = 1;
                            }
                            if (!yMin[y + steps]) {
                                yMin[y + steps] = true;
                                coor[2] = 1;
                            }
                            else if (!yMax[y + steps]) {
                                yMax[y + steps] = true;
                                /* set */ (prevCoor[y + steps] = coor);
                                coor[2] = 1;
                            }
                            else {
                                /* get */ prevCoor[y + steps][2] = 0;
                                /* set */ (prevCoor[y + steps] = coor);
                                coor[2] = 1;
                            }
                            /* add */ (results.push(coor) > 0);
                        }
                        else {
                            if (!minX) {
                                minX = true;
                            }
                            else if (maxX) {
                                /* get */ results[results.length - 1][2] = 0;
                                maxX = false;
                            }
                            if (!yMin[y + steps]) {
                                yMin[y + steps] = true;
                            }
                            else if (yMax[y + steps]) {
                                /* get */ prevCoor[y + steps][2] = 0;
                                yMax[y + steps] = false;
                            }
                        }
                    }
                }
                ;
            }
            ;
            return results;
        };
        Pathing.checkBounds = function (r, x, y, blockers) {
            return (x >= 0 && x < r.map[0].length && y >= 0 && y < r.map.length && r.map[y][x] && !blockers[y][x]);
        };
        Pathing.rangeBFS = function (r, endLocs, range, blockers, t) {
            var startTime = Date.now();
            var nodes = ([]);
            var yBound = r.map.length;
            var xBound = r.map[0].length;
            var lattice = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([r.map.length, r.map[0].length]);
            lattice = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([r.map.length, r.map[0].length]);
            for (var y = 0; y < r.map.length; y++) {
                for (var x = 0; x < r.map[0].length; x++) {
                    if (y % 2 === 0) {
                        if (x % 2 === 0)
                            lattice[y][x] = true;
                    }
                    else {
                        if (x % 2 === 1)
                            lattice[y][x] = true;
                    }
                }
                ;
            }
            ;
            var dirMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([yBound, xBound]);
            for (var i = 0; i < dirMap.length; i++)
                for (var j = 0; j < dirMap[i].length; j++)
                    dirMap[i][j] = 99;
            ;
            for (var y = 0; y < endLocs.length; y++) {
                for (var x = 0; x < endLocs.length; x++) {
                    if (endLocs[y][x]) {
                        var coor = new Array(4);
                        coor[0] = y;
                        coor[1] = x;
                        dirMap[y][x] = 0;
                        /* add */ (nodes.push(coor) > 0);
                        if (r.me.y === y && r.me.x === x)
                            coor[3] = 1;
                    }
                }
                ;
            }
            ;
            var rMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([r.map.length, r.map[0].length]);
            for (var tempY = 0; tempY < rMap.length; tempY++) {
                for (var tempX = 0; tempX < rMap[0].length; tempX++) {
                    if (r.getFuelMap()[tempY][tempX] || r.getKarboniteMap()[tempY][tempX])
                        rMap[tempY][tempX] = true;
                }
                ;
            }
            ;
            var karbMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([r.map.length, r.map[0].length]);
            var fuelMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([r.map.length, r.map[0].length]);
            for (var tempY = 0; tempY < rMap.length; tempY++) {
                for (var tempX = 0; tempX < rMap[0].length; tempX++) {
                    karbMap[tempY][tempX] = r.getKarboniteMap()[tempY][tempX];
                    fuelMap[tempY][tempX] = r.getFuelMap()[tempY][tempX];
                }
                ;
            }
            ;
            while ((!(nodes.length == 0))) {
                var curr = (function (a) { return a.length == 0 ? null : a.shift(); })(nodes);
                var currY = curr[0];
                var currX = curr[1];
                var nextList = Pathing.findRange(r, currX, currY, range, blockers, dirMap);
                var step = dirMap[currY][currX];
                while ((!(nextList.length == 0))) {
                    var coor = (function (a) { return a.length == 0 ? null : a.shift(); })(nextList);
                    var coorY = coor[0];
                    var coorX = coor[1];
                    if (curr[3] === 1)
                        coor[3] = 1;
                    if (dirMap[coorY][coorX] > step + 1) {
                        if (coor[2] === 1)
                            (nodes.push(coor) > 0);
                        dirMap[coorY][coorX] = step + 1;
                        if (t.markKarb && coor[3] === 1) {
                            if (karbMap[coorY][coorX]) {
                                /* add */ (t.karbList.push(new bc19.Resource(coorX, coorY, step)) > 0);
                            }
                        }
                        if (t.markFuel && coor[3] === 1) {
                            if (fuelMap[coorY][coorX]) {
                                /* add */ (t.fuelList.push(new bc19.Resource(coorX, coorY, step + 1)) > 0);
                            }
                        }
                        var s = 6;
                        if ((fuelMap[coorY][coorX] || karbMap[coorY][coorX]) && coor[3] === 1 && Pathing.distance(r.me.x, r.me.y, coorX, coorY) >= s * s && r.me.unit === 0) {
                            for (var dy = -1 * s; dy <= s; dy++) {
                                for (var dx = -1 * s; dx <= s; dx++) {
                                    var checkX = coorX + dx;
                                    var checkY = coorY + dy;
                                    if (Pathing.distance(coorX, coorY, checkX, checkY) <= s * s && Pathing.checkBounds(r, checkX, checkY, blockers)) {
                                        fuelMap[checkY][checkX] = false;
                                        karbMap[checkY][checkX] = false;
                                    }
                                }
                                ;
                            }
                            ;
                        }
                        if (lattice[coorY][coorX] && coor[3] === 1 && !rMap[coorY][coorX] && step + 1 > 1) {
                            var l = new Array(4);
                            l[0] = coorY;
                            l[1] = coorX;
                            l[2] = -1;
                            l[3] = step + 1;
                            /* add */ (t.lattice.push(l) > 0);
                        }
                    }
                }
                ;
            }
            ;
            var diff = Date.now() - startTime;
            r.log("BFS Time: " + diff + " ms");
            return dirMap;
        };
        Pathing.rangeAST = function (r, startX, startY, endX, endY, range, blockers) {
            var startTime = Date.now();
            var path;
            var dirMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([r.map.length, r.map[0].length]);
            for (var i = 0; i < dirMap.length; i++)
                for (var j = 0; j < dirMap[i].length; j++)
                    dirMap[i][j] = 99;
            ;
            var nodes = ([]);
            var start = new Array(3);
            start[0] = startY;
            start[1] = startX;
            /* add */ (nodes.push(start) > 0);
            dirMap[startY][startX] = 0;
            var startList = Pathing.findRange(r, startX, startY, range, blockers, dirMap);
            if (startList.length === 0)
                return null;
            if (blockers[endY][endX])
                return null;
            var _loop_1 = function () {
                var curr = Pathing.findNext(nodes, endX, endY, dirMap);
                /* remove */ (function (a) { return a.splice(a.indexOf(curr), 1); })(nodes);
                var currY = curr[0];
                var currX = curr[1];
                var nextList = Pathing.findRange(r, currX, currY, range, blockers, dirMap);
                var step = dirMap[currY][currX];
                while ((!(nextList.length == 0))) {
                    var coor = (function (a) { return a.length == 0 ? null : a.shift(); })(nextList);
                    var coorY = coor[0];
                    var coorX = coor[1];
                    if (coorX === endX && coorY === endY) {
                        dirMap[coorY][coorX] = 98;
                        path = Pathing.createPath(r, startX, startY, endX, endY, range, dirMap, blockers);
                        var diff = Date.now() - startTime;
                        return { value: path };
                    }
                    if (dirMap[coorY][coorX] > step + 1) {
                        if (coor[2] === 1) {
                            /* add */ (nodes.push(coor) > 0);
                        }
                        dirMap[coorY][coorX] = step + 1;
                    }
                }
                ;
            };
            while ((!(nodes.length == 0))) {
                var state_1 = _loop_1();
                if (typeof state_1 === "object")
                    return state_1.value;
            }
            ;
            return null;
        };
        Pathing.findNext = function (nodes, endX, endY, dirMap) {
            var nextNode;
            var minStep = 9999;
            var minDist = 9999;
            for (var index127 = 0; index127 < nodes.length; index127++) {
                var node = nodes[index127];
                {
                    var newDist = Pathing.distance(node[1], node[0], endX, endY);
                    if (newDist < minDist) {
                        minStep = dirMap[node[0]][node[1]];
                        minDist = newDist;
                        nextNode = node;
                    }
                    else if (newDist === minDist) {
                        var newStep = dirMap[node[0]][node[1]];
                        if (newStep < minStep) {
                            minStep = newStep;
                            nextNode = node;
                        }
                    }
                }
            }
            return nextNode;
        };
        Pathing.createPath = function (r, startX, startY, endX, endY, range, dirMap, blockers) {
            var path = ([]);
            var nodes = ([]);
            var end = new Array(2);
            end[1] = endX;
            end[0] = endY;
            /* add */ (nodes.push(end) > 0);
            while ((!(nodes.length == 0))) {
                var node = (function (a) { return a.length == 0 ? null : a.shift(); })(nodes);
                var nextStep = Pathing.checkAdj(r, node[1], node[0], startX, startY, range, dirMap, blockers);
                nextStep[0] = 0 - nextStep[0];
                nextStep[1] = 0 - nextStep[1];
                /* add */ path.splice(0, 0, nextStep);
                var nextNode = new Array(2);
                nextNode[1] = node[1] - nextStep[1];
                nextNode[0] = node[0] - nextStep[0];
                if (nextNode[1] === startX && nextNode[0] === startY) {
                    return path;
                }
                /* add */ (nodes.push(nextNode) > 0);
            }
            ;
            return path;
        };
        Pathing.checkAdj = function (r, startX, startY, endX, endY, range, dirMap, blockers) {
            var steps = (Math.floor(Math.sqrt(range)) | 0);
            var min = 9999;
            var minDist = 9999;
            var move = new Array(2);
            move[0] = 99;
            for (var y = 0 - steps; y <= steps; y++) {
                for (var x = 0 - steps; x <= steps; x++) {
                    if (x === 0 && y === 0) {
                        continue;
                    }
                    var dist = Pathing.distance(0, 0, x, y);
                    var xCor = startX + x;
                    var yCor = startY + y;
                    if (dist <= range && Pathing.checkBounds(r, xCor, yCor, blockers)) {
                        if (dirMap[yCor][xCor] < min) {
                            min = dirMap[yCor][xCor];
                            minDist = Pathing.distance(xCor, yCor, endX, endY);
                            move[1] = x;
                            move[0] = y;
                        }
                        else if (dirMap[yCor][xCor] === min) {
                            var newDist = Pathing.distance(xCor, yCor, endX, endY);
                            if (newDist < minDist) {
                                minDist = newDist;
                                move[1] = x;
                                move[0] = y;
                            }
                        }
                    }
                }
                ;
            }
            ;
            if (move[0] === 99) {
                return null;
            }
            return move;
        };
        Pathing.retreatMove = function (r, myX, myY, enemies, range, blockers) {
            var steps = (Math.floor(Math.sqrt(range)) | 0);
            var move = new Array(2);
            move[0] = 99;
            var maxDist = 0;
            for (var y = 0 - steps; y <= steps; y++) {
                for (var x = 0 - steps; x <= steps; x++) {
                    if (x === 0 && y === 0) {
                        continue;
                    }
                    var dist = Pathing.distance(0, 0, x, y);
                    var xCor = myX + x;
                    var yCor = myY + y;
                    if (dist <= range && Pathing.checkBounds(r, xCor, yCor, blockers)) {
                        var newDist = 0;
                        for (var i = 0; i < enemies.length; i++) {
                            var enemyCoord = enemies[i];
                            newDist += Pathing.distance(xCor, yCor, enemyCoord[1], enemyCoord[0]);
                        }
                        ;
                        if (newDist > maxDist) {
                            maxDist = newDist;
                            move[1] = x;
                            move[0] = y;
                        }
                    }
                }
                ;
            }
            ;
            if (move[0] === 99) {
                return null;
            }
            return move;
        };
        return Pathing;
    }());
    bc19.Pathing = Pathing;
    Pathing["__class"] = "bc19.Pathing";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var Task = (function () {
        function Task() {
            this.markFuel = false;
            this.markKarb = false;
            this.karbList = null;
            this.fuelList = null;
            this.lattice = null;
            this.markFuel = true;
            this.markKarb = true;
            this.karbList = ([]);
            this.fuelList = ([]);
            this.lattice = ([]);
        }
        return Task;
    }());
    bc19.Task = Task;
    Task["__class"] = "bc19.Task";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var MineAction = (function (_super) {
        __extends(MineAction, _super);
        function MineAction(signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.action = "mine";
            return _this;
        }
        return MineAction;
    }(bc19.Action));
    bc19.MineAction = MineAction;
    MineAction["__class"] = "bc19.MineAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var ErrorAction = (function (_super) {
        __extends(ErrorAction, _super);
        function ErrorAction(error, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.error = null;
            _this.error = error.message;
            return _this;
        }
        return ErrorAction;
    }(bc19.Action));
    bc19.ErrorAction = ErrorAction;
    ErrorAction["__class"] = "bc19.ErrorAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var GiveAction = (function (_super) {
        __extends(GiveAction, _super);
        function GiveAction(giveKarbonite, giveFuel, dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.give_karbonite = 0;
            _this.give_fuel = 0;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "give";
            _this.give_karbonite = giveKarbonite;
            _this.give_fuel = giveFuel;
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return GiveAction;
    }(bc19.Action));
    bc19.GiveAction = GiveAction;
    GiveAction["__class"] = "bc19.GiveAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var BuildAction = (function (_super) {
        __extends(BuildAction, _super);
        function BuildAction(buildUnit, dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.build_unit = 0;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "build";
            _this.build_unit = buildUnit;
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return BuildAction;
    }(bc19.Action));
    bc19.BuildAction = BuildAction;
    BuildAction["__class"] = "bc19.BuildAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var TradeAction = (function (_super) {
        __extends(TradeAction, _super);
        function TradeAction(trade_fuel, trade_karbonite, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.trade_fuel = 0;
            _this.trade_karbonite = 0;
            _this.action = "trade";
            _this.trade_fuel = trade_fuel;
            _this.trade_karbonite = trade_karbonite;
            return _this;
        }
        return TradeAction;
    }(bc19.Action));
    bc19.TradeAction = TradeAction;
    TradeAction["__class"] = "bc19.TradeAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var MoveAction = (function (_super) {
        __extends(MoveAction, _super);
        function MoveAction(dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "move";
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return MoveAction;
    }(bc19.Action));
    bc19.MoveAction = MoveAction;
    MoveAction["__class"] = "bc19.MoveAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var AttackAction = (function (_super) {
        __extends(AttackAction, _super);
        function AttackAction(dx, dy, signal, signalRadius, logs, castleTalk) {
            var _this = _super.call(this, signal, signalRadius, logs, castleTalk) || this;
            _this.action = null;
            _this.dx = 0;
            _this.dy = 0;
            _this.action = "attack";
            _this.dx = dx;
            _this.dy = dy;
            return _this;
        }
        return AttackAction;
    }(bc19.Action));
    bc19.AttackAction = AttackAction;
    AttackAction["__class"] = "bc19.AttackAction";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var MyRobot = (function (_super) {
        __extends(MyRobot, _super);
        function MyRobot() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.bot = null;
            return _this;
        }
        MyRobot.prototype.turn = function () {
            if (this.bot == null) {
                if (this.me.unit === this.SPECS.CASTLE)
                    this.bot = new bc19.CastleBot(this);
                if (this.me.unit === this.SPECS.CHURCH)
                    this.bot = new bc19.ChurchBot(this);
                if (this.me.unit === this.SPECS.PILGRIM)
                    this.bot = new bc19.PilgrimBot(this);
                if (this.me.unit === this.SPECS.CRUSADER)
                    this.bot = new bc19.CrusaderBot(this);
                if (this.me.unit === this.SPECS.PROPHET)
                    this.bot = new bc19.ProphetBot(this);
                if (this.me.unit === this.SPECS.PREACHER)
                    this.bot = new bc19.PreacherBot(this);
            }
            else {
                this.bot.update(this);
            }
            return this.bot.act();
        };
        return MyRobot;
    }(bc19.BCAbstractRobot));
    bc19.MyRobot = MyRobot;
    MyRobot["__class"] = "bc19.MyRobot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var ChurchBot = (function (_super) {
        __extends(ChurchBot, _super);
        function ChurchBot(r) {
            var _this = _super.call(this, r) || this;
            _this.lattice = null;
            _this.karbList = null;
            _this.aKarbList = null;
            _this.fuelList = null;
            _this.aFuelList = null;
            _this.resMap = null;
            _this.allocate = null;
            _this.allocLat = null;
            _this.builder = 0;
            return _this;
        }
        ChurchBot.prototype.act = function () {
            var visible = this.r.getVisibleRobots();
            if (this.me.turn === 1) {
                var b = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                var endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                var sig = void 0;
                for (var index128 = 0; index128 < visible.length; index128++) {
                    var other = visible[index128];
                    {
                        if (other.team === this.me.team && other.unit === 2 && this.r.isRadioing(other)) {
                            if (other.signal === 2)
                                this.even = true;
                            this.builder = other.id;
                        }
                    }
                }
                var builderRobot = this.r.getRobot(this.builder);
                endLocs[this.me.y][this.me.x] = true;
                var t = new bc19.Task();
                this.resMap = bc19.Pathing.rangeBFS(this.r, endLocs, 4, b, t);
                this.karbList = t.karbList;
                this.fuelList = t.fuelList;
                for (var i = 0; i < this.karbList.length; i++) {
                    var res = this.karbList[i];
                    if (bc19.Pathing.distance(res.x, res.y, this.me.x, this.me.y) >= 36) {
                        /* remove */ this.karbList.splice(i, 1);
                        i--;
                    }
                    else if (builderRobot != null && builderRobot.x === res.x && builderRobot.y === res.y) {
                        /* remove */ this.karbList.splice(i, 1);
                        i--;
                    }
                }
                ;
                for (var i = 0; i < this.fuelList.length; i++) {
                    var res = this.fuelList[i];
                    if (bc19.Pathing.distance(res.x, res.y, this.me.x, this.me.y) >= 36) {
                        /* remove */ this.fuelList.splice(i, 1);
                        i--;
                    }
                    else if (builderRobot != null && builderRobot.x === res.x && builderRobot.y === res.y) {
                        /* remove */ this.fuelList.splice(i, 1);
                        i--;
                    }
                }
                ;
                this.aKarbList = ([]);
                this.aFuelList = ([]);
                this.lattice = t.lattice;
            }
            else
                this.even = !this.even;
            if (!(function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(this.allocLat, null)) {
                for (var index129 = 0; index129 < visible.length; index129++) {
                    var other = visible[index129];
                    {
                        if (other.unit > 2 && bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y) <= 36 && other.team === this.me.team && other.turn === 1) {
                            this.allocLat[2] = other.id;
                            break;
                        }
                    }
                }
                this.allocLat = null;
            }
            if (!(function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(this.allocate, null) && !(function (a1, a2) { if (a1 == null && a2 == null)
                return true; if (a1 == null || a2 == null)
                return false; if (a1.length != a2.length)
                return false; for (var i = 0; i < a1.length; i++) {
                if (a1[i] != a2[i])
                    return false;
            } return true; })(this.aKarbList, null)) {
                var resList = void 0;
                var otherList = void 0;
                if (this.allocate.isKarb) {
                    resList = this.aKarbList;
                    otherList = this.aFuelList;
                }
                else {
                    resList = this.aFuelList;
                    otherList = this.aKarbList;
                }
                for (var index130 = 0; index130 < visible.length; index130++) {
                    var other = visible[index130];
                    {
                        if (other.unit === 2 && bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y) <= 36 && other.team === this.me.team) {
                            var dup = false;
                            for (var index131 = 0; index131 < resList.length; index131++) {
                                var res = resList[index131];
                                if (other.id === res.worker) {
                                    dup = true;
                                }
                            }
                            for (var index132 = 0; index132 < otherList.length; index132++) {
                                var res = otherList[index132];
                                if (other.id === res.worker) {
                                    dup = true;
                                }
                            }
                            if (!dup) {
                                this.allocate.worker = other.id;
                                break;
                            }
                        }
                    }
                }
                this.allocate = null;
            }
            for (var index133 = 0; index133 < this.aKarbList.length; index133++) {
                var res = this.aKarbList[index133];
                {
                    if (res.worker !== -1 && !(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(res, this.allocate)) {
                        var other = this.r.getRobot(res.worker);
                        if ((function (o1, o2) { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(other, null))
                            res.worker = -1;
                    }
                }
            }
            for (var index134 = 0; index134 < this.aFuelList.length; index134++) {
                var res = this.aFuelList[index134];
                {
                    if (res.worker !== -1 && !(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(res, this.allocate)) {
                        var other = this.r.getRobot(res.worker);
                        if ((function (o1, o2) { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(other, null))
                            res.worker = -1;
                    }
                }
            }
            for (var index135 = 0; index135 < this.lattice.length; index135++) {
                var c = this.lattice[index135];
                {
                    if (c[2] !== -1) {
                        if ((function (o1, o2) { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(this.r.getRobot(c[2]), null) && bc19.Pathing.distance(c[1], c[0], this.me.x, this.me.y) <= 81)
                            c[2] = -1;
                    }
                }
            }
            var preacher = false;
            var enemySighted = false;
            var enemyCount = 0;
            var allyCount = 0;
            var enemies = ([]);
            for (var index136 = 0; index136 < visible.length; index136++) {
                var other = visible[index136];
                {
                    if (other.team !== this.me.team) {
                        enemySighted = true;
                        enemyCount++;
                        var enemyY = other.y;
                        var enemyX = other.x;
                        var enemyCor = new Array(2);
                        enemyCor[0] = enemyY;
                        enemyCor[1] = enemyX;
                        /* add */ (enemies.push(enemyCor) > 0);
                        if (other.unit === 5 && bc19.Pathing.distance(this.me.x, this.me.y, other.x, other.y) <= 16) {
                            preacher = true;
                            break;
                        }
                    }
                    else if (other.unit > 2)
                        allyCount++;
                }
            }
            if (this.r.karbonite >= 25 && this.r.fuel >= 50 && !preacher) {
                var a = null;
                if (enemySighted && allyCount < enemyCount) {
                    if (this.r.fuel > 200) {
                        a = this.spawnSoldier(4, enemies);
                    }
                }
                else if (this.me.turn % 15 === 0 || this.r.karbonite > 250) {
                    a = this.spawnSoldier(4, enemies);
                }
                if (!(function (o1, o2) { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(a, null)) {
                    return a;
                }
            }
            if (this.r.karbonite >= 10 && this.r.fuel >= 50) {
                var deadKarb = false;
                for (var index137 = 0; index137 < this.aKarbList.length; index137++) {
                    var res = this.aKarbList[index137];
                    {
                        if (res.worker === -1)
                            deadKarb = true;
                    }
                }
                if (deadKarb || this.karbList.length > 0) {
                    var a = this.spawnWorker(true);
                    if (!(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(a, null)) {
                        var s = 50000;
                        if (this.even)
                            s = 0;
                        s += this.allocate.x * 100;
                        s += this.allocate.y;
                        this.r.signal(s, 2);
                        return a;
                    }
                }
                var deadFuel = false;
                for (var index138 = 0; index138 < this.aFuelList.length; index138++) {
                    var res = this.aFuelList[index138];
                    {
                        if (res.worker === -1)
                            deadFuel = true;
                    }
                }
                if (deadFuel || this.fuelList.length > 0) {
                    var a = this.spawnWorker(false);
                    if (!(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(a, null)) {
                        var s = 50000;
                        if (this.even)
                            s = 0;
                        s += this.allocate.x * 100;
                        s += this.allocate.y;
                        this.r.signal(s, 2);
                        return a;
                    }
                }
            }
            return null;
        };
        ChurchBot.prototype.spawnSoldier = function (unit, enemies) {
            var target = null;
            for (var i = 0; i < this.lattice.length; i++) {
                var c = this.lattice[i];
                if (c[2] === -1) {
                    if (this.lblockers[c[0]][c[1]])
                        continue;
                    if (bc19.Pathing.distance(c[1], c[0], this.me.x, this.me.y) > 100) {
                        /* remove */ this.lattice.splice(i, 1);
                        i--;
                    }
                    else {
                        this.allocLat = c;
                        target = c;
                        break;
                    }
                }
            }
            ;
            if ((function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(target, null))
                return null;
            if (enemies.length === 0) {
                this.blockers[target[0]][target[1]] = false;
                var path = bc19.Pathing.rangeAST(this.r, this.me.x, this.me.y, target[1], target[0], 2, this.blockers);
                if (!(function (a1, a2) { if (a1 == null && a2 == null)
                    return true; if (a1 == null || a2 == null)
                    return false; if (a1.length != a2.length)
                    return false; for (var i = 0; i < a1.length; i++) {
                    if (a1[i] != a2[i])
                        return false;
                } return true; })(path, null) && path.length > 0) {
                    var coord = (function (a) { return a.length == 0 ? null : a.shift(); })(path);
                    var s = 40000;
                    if (this.even)
                        s = 20000;
                    s += target[1] * 100 + target[0];
                    this.r.signal(s, 2);
                    return this.r.buildUnit(unit, coord[1], coord[0]);
                }
            }
            else {
                var go = bc19.Pathing.retreatMove(this.r, this.me.x, this.me.y, enemies, 2, this.blockers);
                if (!(function (o1, o2) { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(go, null)) {
                    var s = 40000;
                    if (this.even)
                        s = 20000;
                    s += target[1] * 100 + target[0];
                    this.r.signal(s, 2);
                    return this.r.buildUnit(unit, go[1], go[0]);
                }
            }
            return null;
        };
        ChurchBot.prototype.spawnWorker = function (karb) {
            var resList;
            if (karb)
                resList = this.aKarbList;
            else
                resList = this.aFuelList;
            var found = false;
            for (var i = 0; i < resList.length; i++) {
                var res = resList[i];
                if (res.worker === -1) {
                    this.allocate = res;
                    this.allocate.isKarb = karb;
                    found = true;
                    break;
                }
            }
            ;
            if (!found) {
                var oldList = void 0;
                if (karb)
                    oldList = this.karbList;
                else
                    oldList = this.fuelList;
                if (oldList.length > 0) {
                    this.allocate = (function (a) { return a.length == 0 ? null : a.shift(); })(oldList);
                    this.allocate.isKarb = karb;
                    /* add */ (resList.push(this.allocate) > 0);
                }
                else
                    return null;
            }
            var nullMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
            for (var y = 0; y < nullMap.length; y++) {
                for (var x = 0; x < nullMap.length; x++) {
                    nullMap[y][x] = 99;
                }
                ;
            }
            ;
            var path = bc19.Pathing.rangeAST(this.r, this.me.x, this.me.y, this.allocate.x, this.allocate.y, 2, this.blockers);
            if (!(function (a1, a2) { if (a1 == null && a2 == null)
                return true; if (a1 == null || a2 == null)
                return false; if (a1.length != a2.length)
                return false; for (var i = 0; i < a1.length; i++) {
                if (a1[i] != a2[i])
                    return false;
            } return true; })(path, null) && path.length > 0) {
                var coord = (function (a) { return a.length == 0 ? null : a.shift(); })(path);
                return this.r.buildUnit(2, coord[1], coord[0]);
            }
            return null;
        };
        return ChurchBot;
    }(bc19.Bot));
    bc19.ChurchBot = ChurchBot;
    ChurchBot["__class"] = "bc19.ChurchBot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var CrusaderBot = (function (_super) {
        __extends(CrusaderBot, _super);
        function CrusaderBot(r) {
            return _super.call(this, r) || this;
        }
        CrusaderBot.prototype.act = function () {
            return null;
        };
        return CrusaderBot;
    }(bc19.Bot));
    bc19.CrusaderBot = CrusaderBot;
    CrusaderBot["__class"] = "bc19.CrusaderBot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var CastleBot = (function (_super) {
        __extends(CastleBot, _super);
        function CastleBot(r) {
            var _this = _super.call(this, r) || this;
            _this.karbList = null;
            _this.aKarbList = null;
            _this.fuelList = null;
            _this.aFuelList = null;
            _this.lattice = null;
            _this.resMap = null;
            _this.allocate = null;
            _this.allocLat = null;
            _this.attacked = false;
            _this.attackFinished = false;
            _this.xCoors = null;
            _this.yCoors = null;
            _this.signalCount = 0;
            return _this;
        }
        CastleBot.prototype.act = function () {
            var visible = this.r.getVisibleRobots();
            this.r.log("Turn: " + this.me.turn);
            if (this.signalCount === 0)
                this.signalCount = -1;
            if (this.me.turn === 1) {
                this.xCoors = (function (s) { var a = []; while (s-- > 0)
                    a.push(0); return a; })(5000);
                this.yCoors = (function (s) { var a = []; while (s-- > 0)
                    a.push(0); return a; })(5000);
                for (var i = 0; i < this.xCoors.length; i++)
                    this.xCoors[i] = -1;
                for (var i = 0; i < this.yCoors.length; i++)
                    this.yCoors[i] = -1;
                this.symmetry = bc19.Logistics.symmetry(this.r.map, this.r);
                this.enemyCastles = bc19.Logistics.findOpposite(this.r, this.me.x, this.me.y, this.symmetry);
                this.myCastles = ([]);
                var myCor = new Array(3);
                myCor[0] = this.me.y;
                myCor[1] = this.me.x;
                myCor[2] = this.me.id;
                /* add */ (this.myCastles.push(myCor) > 0);
                var firstCastle = true;
                var count = 1;
                for (var index139 = 0; index139 < visible.length; index139++) {
                    var other = visible[index139];
                    {
                        if (other.castle_talk !== 0 && other.id !== this.r.id) {
                            firstCastle = false;
                            var c = (Math.floor((other.castle_talk / 100 | 0)) | 0);
                            if (c === 0)
                                this.numCastles = 2;
                            else
                                this.numCastles = 3;
                            var coor = new Array(3);
                            coor[0] = -1;
                            coor[1] = other.castle_talk % 100 - 1;
                            coor[2] = other.id;
                            /* add */ (this.myCastles.push(coor) > 0);
                        }
                        if (!this.r.isVisible(other))
                            count++;
                    }
                }
                if (firstCastle) {
                    this.numCastles = count;
                }
                if (this.numCastles === 2)
                    this.r.castleTalk(this.me.x + 1);
                if (this.numCastles === 3)
                    this.r.castleTalk(this.me.x + 101);
                var t = new bc19.Task();
                var b = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                var endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                for (var y = 0; y < b.length; y++) {
                    for (var x = 0; x < b[0].length; x++) {
                        for (var index140 = 0; index140 < this.enemyCastles.length; index140++) {
                            var c = this.enemyCastles[index140];
                            {
                                if (bc19.Pathing.distance(c[1], c[0], x, y) <= 100)
                                    b[y][x] = true;
                            }
                        }
                    }
                    ;
                }
                ;
                endLocs[this.me.y][this.me.x] = true;
                this.resMap = bc19.Pathing.rangeBFS(this.r, endLocs, 4, b, t);
                this.karbList = t.karbList;
                for (var index141 = 0; index141 < this.karbList.length; index141++) {
                    var res = this.karbList[index141];
                    res.isKarb = true;
                }
                this.fuelList = t.fuelList;
                this.aKarbList = ([]);
                this.aFuelList = ([]);
                this.lattice = t.lattice;
                if (this.numCastles === 1) {
                    var printLattice = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                        return 0;
                    }
                    else {
                        var array = [];
                        for (var i = 0; i < dims[0]; i++) {
                            array.push(allocate(dims.slice(1)));
                        }
                        return array;
                    } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                    for (var index142 = 0; index142 < this.lattice.length; index142++) {
                        var l = this.lattice[index142];
                        printLattice[l[0]][l[1]] = l[3];
                    }
                    bc19.Pathing.printMap(printLattice, this.r);
                }
            }
            else if (this.me.turn <= 3) {
                for (var index143 = 0; index143 < visible.length; index143++) {
                    var other = visible[index143];
                    {
                        if (other.castle_talk !== 0 && other.id !== this.r.id) {
                            var c = other.castle_talk % 100 - 1;
                            var found = false;
                            for (var index144 = 0; index144 < this.myCastles.length; index144++) {
                                var l = this.myCastles[index144];
                                {
                                    if (l.length === 3 && (function (o1, o2) { if (o1 && o1.equals) {
                                        return o1.equals(o2);
                                    }
                                    else {
                                        return o1 === o2;
                                    } })(l[2], other.id)) {
                                        found = true;
                                        l[0] = c;
                                        break;
                                    }
                                }
                            }
                            if (!found) {
                                var coor = new Array(3);
                                coor[0] = -1;
                                coor[1] = other.castle_talk % 100 - 1;
                                coor[2] = other.id;
                                /* add */ (this.myCastles.push(coor) > 0);
                            }
                        }
                    }
                }
            }
            if (!(function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(this.allocLat, null)) {
                for (var index145 = 0; index145 < visible.length; index145++) {
                    var other = visible[index145];
                    {
                        if (other.unit > 2 && bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y) <= 36 && other.team === this.me.team && other.turn === 1) {
                            this.allocLat[2] = other.id;
                            break;
                        }
                    }
                }
                this.allocLat = null;
            }
            if (!(function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(this.allocate, null) && !(function (a1, a2) { if (a1 == null && a2 == null)
                return true; if (a1 == null || a2 == null)
                return false; if (a1.length != a2.length)
                return false; for (var i = 0; i < a1.length; i++) {
                if (a1[i] != a2[i])
                    return false;
            } return true; })(this.aKarbList, null)) {
                var resList = void 0;
                var otherList = void 0;
                if (this.allocate.isKarb) {
                    resList = this.aKarbList;
                    otherList = this.aFuelList;
                }
                else {
                    resList = this.aFuelList;
                    otherList = this.aKarbList;
                }
                for (var index146 = 0; index146 < visible.length; index146++) {
                    var other = visible[index146];
                    {
                        if (other.unit === 2 && bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y) <= 36 && other.team === this.me.team) {
                            var dup = false;
                            for (var index147 = 0; index147 < resList.length; index147++) {
                                var res = resList[index147];
                                if (other.id === res.worker) {
                                    dup = true;
                                }
                            }
                            for (var index148 = 0; index148 < otherList.length; index148++) {
                                var res = otherList[index148];
                                if (other.id === res.worker) {
                                    dup = true;
                                }
                            }
                            if (!dup) {
                                this.allocate.worker = other.id;
                                break;
                            }
                        }
                    }
                }
                this.allocate = null;
            }
            if (this.me.turn === 2) {
                this.r.castleTalk(this.me.y + 1);
            }
            if (!this.fullyInit) {
                this.fullyInit = this.fullyInitialize();
                if (this.fullyInit) {
                    for (var index149 = 0; index149 < this.myCastles.length; index149++) {
                        var c = this.myCastles[index149];
                        {
                            if (c[0] === this.me.y && c[1] === this.me.x)
                                continue;
                            /* add */ (this.enemyCastles.push(/* poll */ (function (a) { return a.length == 0 ? null : a.shift(); })(bc19.Logistics.findOpposite(this.r, c[1], c[0], this.symmetry))) > 0);
                        }
                    }
                    if (this.numCastles > 1) {
                        var t = new bc19.Task();
                        var b = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                            return undefined;
                        }
                        else {
                            var array = [];
                            for (var i = 0; i < dims[0]; i++) {
                                array.push(allocate(dims.slice(1)));
                            }
                            return array;
                        } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                        for (var y = 0; y < b.length; y++) {
                            for (var x = 0; x < b[0].length; x++) {
                                for (var index150 = 0; index150 < this.enemyCastles.length; index150++) {
                                    var c = this.enemyCastles[index150];
                                    {
                                        if (bc19.Pathing.distance(c[1], c[0], x, y) <= 100)
                                            b[y][x] = true;
                                    }
                                }
                            }
                            ;
                        }
                        ;
                        var endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                            return undefined;
                        }
                        else {
                            var array = [];
                            for (var i = 0; i < dims[0]; i++) {
                                array.push(allocate(dims.slice(1)));
                            }
                            return array;
                        } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                        for (var index151 = 0; index151 < this.myCastles.length; index151++) {
                            var c = this.myCastles[index151];
                            endLocs[c[0]][c[1]] = true;
                        }
                        this.resMap = bc19.Pathing.rangeBFS(this.r, endLocs, 4, b, t);
                        this.karbList = t.karbList;
                        for (var index152 = 0; index152 < this.karbList.length; index152++) {
                            var res = this.karbList[index152];
                            res.isKarb = true;
                        }
                        /* poll */ (function (a) { return a.length == 0 ? null : a.shift(); })(this.karbList);
                        this.fuelList = t.fuelList;
                        this.lattice = t.lattice;
                        var printLattice = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                            return 0;
                        }
                        else {
                            var array = [];
                            for (var i = 0; i < dims[0]; i++) {
                                array.push(allocate(dims.slice(1)));
                            }
                            return array;
                        } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                        for (var index153 = 0; index153 < this.lattice.length; index153++) {
                            var l = this.lattice[index153];
                            printLattice[l[0]][l[1]] = l[3];
                        }
                        bc19.Pathing.printMap(printLattice, this.r);
                    }
                }
            }
            if (this.fullyInit) {
                for (var index154 = 0; index154 < visible.length; index154++) {
                    var other = visible[index154];
                    {
                        if (other.team === this.me.team && other.id !== this.me.id) {
                            if (other.castle_talk === 200) {
                                this.r.signal(7777, 25);
                            }
                            else if (this.me.turn % 2 === 1) {
                                this.xCoors[other.id] = other.castle_talk;
                            }
                            else {
                                this.yCoors[other.id] = other.castle_talk;
                            }
                        }
                    }
                }
                for (var index155 = 0; index155 < this.aKarbList.length; index155++) {
                    var res = this.aKarbList[index155];
                    {
                        if (res.worker !== -1 && !(function (o1, o2) { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(res, this.allocate)) {
                            var other = this.r.getRobot(res.worker);
                            if ((function (o1, o2) { if (o1 && o1.equals) {
                                return o1.equals(o2);
                            }
                            else {
                                return o1 === o2;
                            } })(other, null))
                                res.worker = -1;
                        }
                    }
                }
                for (var index156 = 0; index156 < this.aFuelList.length; index156++) {
                    var res = this.aFuelList[index156];
                    {
                        if (res.worker !== -1 && !(function (o1, o2) { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(res, this.allocate)) {
                            var other = this.r.getRobot(res.worker);
                            if ((function (o1, o2) { if (o1 && o1.equals) {
                                return o1.equals(o2);
                            }
                            else {
                                return o1 === o2;
                            } })(other, null))
                                res.worker = -1;
                        }
                    }
                }
                for (var index157 = 0; index157 < this.lattice.length; index157++) {
                    var c = this.lattice[index157];
                    {
                        if (c[2] !== -1) {
                            if ((function (o1, o2) { if (o1 && o1.equals) {
                                return o1.equals(o2);
                            }
                            else {
                                return o1 === o2;
                            } })(this.r.getRobot(c[2]), null))
                                c[2] = -1;
                        }
                    }
                }
                if (this.me.turn === 850) {
                    var blitz = false;
                    for (var index158 = 0; index158 < this.myCastles.length; index158++) {
                        var c = this.myCastles[index158];
                        if (this.r.getRobot(c[2]) == null)
                            blitz = true;
                    }
                    if (blitz) {
                        if (this.numCastles === 1) {
                            var enemy = this.enemyCastles[0];
                            this.r.signal(30000 + enemy[1] * 100 + enemy[0], 64);
                            return null;
                        }
                        else
                            this.signalCount = this.numCastles - 1;
                    }
                }
                if (this.signalCount > 0) {
                    var s = 10000;
                    if (this.signalCount === 1)
                        s = 30000;
                    var c = this.enemyCastles[this.signalCount];
                    s += c[1] * 100 + c[0];
                    this.r.signal(s, 16);
                    this.signalCount--;
                    return null;
                }
                var preacher = false;
                var enemySighted = false;
                var enemyCount = 0;
                var allyCount = 0;
                var enemies = ([]);
                for (var index159 = 0; index159 < visible.length; index159++) {
                    var other = visible[index159];
                    {
                        if (other.team !== this.me.team) {
                            enemySighted = true;
                            enemyCount++;
                            var enemyY = other.y;
                            var enemyX = other.x;
                            var enemyCor = new Array(2);
                            enemyCor[0] = enemyY;
                            enemyCor[1] = enemyX;
                            /* add */ (enemies.push(enemyCor) > 0);
                            if (other.unit === 5 && bc19.Pathing.distance(this.me.x, this.me.y, other.x, other.y) <= 16) {
                                preacher = true;
                                break;
                            }
                        }
                        else if (other.unit > 2)
                            allyCount++;
                    }
                }
                if (enemySighted && !this.attacked) {
                    this.attacked = true;
                    this.r.castleTalk(200);
                }
                else if (this.attacked && !enemySighted)
                    this.attackFinished = true;
                if (this.r.karbonite >= 25 && this.r.fuel >= 50 && !preacher) {
                    var a = null;
                    if (enemySighted && allyCount < enemyCount + 1) {
                        if (this.r.fuel > 200) {
                            a = this.spawnSoldier(4, enemies);
                        }
                    }
                    else if (this.me.turn % 10 === 0 && (this.r.karbonite > 100 || (this.attackFinished && this.r.karbonite >= 50 && allyCount < 5)))
                        a = this.spawnSoldier(4, enemies);
                    else if (this.r.karbonite > 300)
                        a = this.spawnSoldier(4, enemies);
                    if (!(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(a, null)) {
                        return a;
                    }
                }
                if (enemySighted && this.r.fuel >= 10) {
                    var a = this.attack();
                    if (!(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(a, null))
                        return a;
                }
            }
            if (this.r.karbonite >= 10 && this.r.fuel >= 50 && (this.me.turn === 1 || this.fullyInit)) {
                var nextFuel = null;
                for (var index160 = 0; index160 < this.aFuelList.length; index160++) {
                    var res = this.aFuelList[index160];
                    {
                        if (res.worker === -1 && !res.replaced) {
                            nextFuel = res;
                            break;
                        }
                    }
                }
                if (nextFuel == null && this.fuelList != null && this.fuelList.length > 0) {
                    nextFuel = this.fuelList[0];
                }
                var nextKarb = null;
                for (var index161 = 0; index161 < this.aKarbList.length; index161++) {
                    var res = this.aKarbList[index161];
                    {
                        if (res.worker === -1 && !res.replaced) {
                            nextKarb = res;
                            break;
                        }
                    }
                }
                if (nextKarb == null && this.karbList != null && this.karbList.length > 0) {
                    nextKarb = this.karbList[0];
                }
                var next = null;
                if (nextKarb == null)
                    next = nextFuel;
                else if (nextFuel == null)
                    next = nextKarb;
                else {
                    if (nextKarb.priority <= nextFuel.priority)
                        next = nextKarb;
                    else
                        next = nextFuel;
                }
                if (next != null && (this.r.karbonite >= 85 || (bc19.Pathing.distance(next.x, next.y, this.me.x, this.me.y) <= 36 && this.r.karbonite >= 35) || (this.attackFinished && this.r.karbonite >= 60))) {
                    var a = this.spawnWorker(next.isKarb);
                    if (!(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(a, null)) {
                        var s = 50000;
                        if (this.me.turn % 2 === 0)
                            s = 0;
                        s += this.allocate.x * 100;
                        s += this.allocate.y;
                        this.r.signal(s, 2);
                        return a;
                    }
                }
            }
            return null;
        };
        CastleBot.prototype.fullyInitialize = function () {
            for (var index162 = 0; index162 < this.myCastles.length; index162++) {
                var c = this.myCastles[index162];
                {
                    if (c[0] === -1 || c[1] === -1)
                        return false;
                }
            }
            if (this.myCastles.length < this.numCastles)
                return false;
            return true;
        };
        CastleBot.prototype.spawnSoldier = function (unit, enemies) {
            var target = null;
            var min = 99999;
            var min2 = 99999;
            for (var i = 0; i < this.lattice.length; i++) {
                var c = this.lattice[i];
                if (c[2] === -1) {
                    if (bc19.Pathing.distance(this.me.x, this.me.y, c[1], c[0]) > 100) {
                        var found = false;
                        for (var j = 0; j < this.xCoors.length; j++) {
                            if (this.r.getRobot(j) == null) {
                                this.xCoors[j] = -1;
                                this.yCoors[j] = -1;
                            }
                            if (this.xCoors[j] === c[1] && this.yCoors[j] === c[0])
                                found = true;
                        }
                        ;
                        if (found)
                            continue;
                    }
                    if (this.lblockers[c[0]][c[1]]) {
                        continue;
                    }
                    var sum = 0;
                    for (var index163 = 0; index163 < this.enemyCastles.length; index163++) {
                        var e = this.enemyCastles[index163];
                        {
                            sum += bc19.Pathing.distance(c[1], c[0], e[1], e[0]);
                        }
                    }
                    if (bc19.Pathing.distance(this.me.x, this.me.y, c[1], c[0]) <= min) {
                        min = bc19.Pathing.distance(this.me.x, this.me.y, c[1], c[0]);
                        if (sum < min2) {
                            this.allocLat = c;
                            target = c;
                            min2 = sum;
                        }
                    }
                    else
                        break;
                }
            }
            ;
            if ((function (o1, o2) { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(target, null))
                return null;
            if (enemies.length === 0) {
                this.blockers[target[0]][target[1]] = false;
                var path = bc19.Pathing.rangeAST(this.r, this.me.x, this.me.y, target[1], target[0], 2, this.blockers);
                if (!(function (a1, a2) { if (a1 == null && a2 == null)
                    return true; if (a1 == null || a2 == null)
                    return false; if (a1.length != a2.length)
                    return false; for (var i = 0; i < a1.length; i++) {
                    if (a1[i] != a2[i])
                        return false;
                } return true; })(path, null) && path.length > 0) {
                    var coord = (function (a) { return a.length == 0 ? null : a.shift(); })(path);
                    var s = 40000;
                    if (this.me.turn % 2 === 0)
                        s = 20000;
                    s += target[1] * 100 + target[0];
                    this.r.signal(s, 2);
                    return this.r.buildUnit(unit, coord[1], coord[0]);
                }
            }
            else {
                var go = bc19.Pathing.retreatMove(this.r, this.me.x, this.me.y, enemies, 2, this.blockers);
                if (!(function (o1, o2) { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(go, null)) {
                    var s = 40000;
                    if (this.me.turn % 2 === 0)
                        s = 20000;
                    s += target[1] * 100 + target[0];
                    this.r.signal(s, 2);
                    return this.r.buildUnit(unit, go[1], go[0]);
                }
            }
            return null;
        };
        CastleBot.prototype.spawnWorker = function (karb) {
            var resList;
            if (karb)
                resList = this.aKarbList;
            else
                resList = this.aFuelList;
            var found = false;
            for (var i = 0; i < resList.length; i++) {
                var res = resList[i];
                if (res.worker === -1) {
                    if (bc19.Pathing.distance(res.x, res.y, this.me.x, this.me.y) > 36) {
                        if (!res.replaced) {
                            res.replaced = true;
                            this.allocate = res;
                            this.allocate.isKarb = karb;
                            found = true;
                            break;
                        }
                        else {
                            /* remove */ resList.splice(i, 1);
                            i--;
                        }
                    }
                    else {
                        this.allocate = res;
                        this.allocate.isKarb = karb;
                        found = true;
                        break;
                    }
                }
            }
            ;
            if (!found) {
                var oldList = void 0;
                if (karb)
                    oldList = this.karbList;
                else
                    oldList = this.fuelList;
                if (oldList.length > 0) {
                    this.allocate = (function (a) { return a.length == 0 ? null : a.shift(); })(oldList);
                    this.allocate.isKarb = karb;
                    for (var index164 = 0; index164 < this.enemyCastles.length; index164++) {
                        var c = this.enemyCastles[index164];
                        {
                            if (bc19.Pathing.distance(c[1], c[0], this.allocate.x, this.allocate.y) <= 144) {
                                this.allocate = null;
                                return null;
                            }
                        }
                    }
                    /* add */ (resList.push(this.allocate) > 0);
                }
                else
                    return null;
            }
            var nullMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
            for (var y = 0; y < nullMap.length; y++) {
                for (var x = 0; x < nullMap.length; x++) {
                    nullMap[y][x] = 99;
                }
                ;
            }
            ;
            var path = bc19.Pathing.rangeAST(this.r, this.me.x, this.me.y, this.allocate.x, this.allocate.y, 2, this.blockers);
            if (!(function (a1, a2) { if (a1 == null && a2 == null)
                return true; if (a1 == null || a2 == null)
                return false; if (a1.length != a2.length)
                return false; for (var i = 0; i < a1.length; i++) {
                if (a1[i] != a2[i])
                    return false;
            } return true; })(path, null) && path.length > 0) {
                var coord = (function (a) { return a.length == 0 ? null : a.shift(); })(path);
                return this.r.buildUnit(2, coord[1], coord[0]);
            }
            return null;
        };
        return CastleBot;
    }(bc19.Bot));
    bc19.CastleBot = CastleBot;
    CastleBot["__class"] = "bc19.CastleBot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var PilgrimBot = (function (_super) {
        __extends(PilgrimBot, _super);
        function PilgrimBot(r) {
            var _this = _super.call(this, r) || this;
            _this.toGo = null;
            _this.deposit = false;
            _this.running = false;
            _this.endLocs = null;
            _this.castle = null;
            _this.Rmap = null;
            _this.Cmap = null;
            _this.deposit = false;
            return _this;
        }
        PilgrimBot.prototype.extractSignal = function (signal) {
            if ((Math.floor((signal / 10000 | 0)) | 0) === 0)
                this.even = true;
            var sig = signal % 10000;
            var ycor = sig % 100;
            var xcor = (Math.floor(((sig - ycor) / 100 | 0)) | 0);
            this.toGo[1] = xcor;
            this.toGo[0] = ycor;
        };
        PilgrimBot.prototype.nextMove = function (map, dest) {
            var min = 9999;
            var minDist = 9999;
            var curr;
            var nullMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
            for (var y = 0; y < nullMap.length; y++) {
                for (var x = 0; x < nullMap.length; x++) {
                    nullMap[y][x] = 99;
                }
                ;
            }
            ;
            var moves = bc19.Pathing.findRange(this.r, this.me.x, this.me.y, 4, this.blockers, nullMap);
            for (var index165 = 0; index165 < moves.length; index165++) {
                var x = moves[index165];
                {
                    if (map[x[0]][x[1]] < min) {
                        min = map[x[0]][x[1]];
                        minDist = bc19.Pathing.distance(x[1], x[0], dest[1], dest[0]);
                        curr = x;
                    }
                    if (map[x[0]][x[1]] === min) {
                        var newDist = bc19.Pathing.distance(x[1], x[0], dest[1], dest[0]);
                        if (newDist < minDist) {
                            minDist = newDist;
                            curr = x;
                        }
                    }
                }
            }
            if (map[curr[0]][curr[1]] === map[this.me.y][this.me.x]) {
                if (bc19.Pathing.distance(curr[1], curr[0], dest[1], dest[0]) === bc19.Pathing.distance(this.me.x, this.me.y, dest[1], dest[0])) {
                    return null;
                }
            }
            return curr;
        };
        PilgrimBot.prototype.act = function () {
            var visible = this.r.getVisibleRobots();
            if (this.me.turn === 1) {
                this.toGo = new Array(2);
                this.castle = new Array(2);
                this.symmetry = bc19.Logistics.symmetry(this.r.map, this.r);
                for (var index166 = 0; index166 < visible.length; index166++) {
                    var c = visible[index166];
                    {
                        if (c.unit <= 1 && this.r.isRadioing(c) && bc19.Pathing.distance(c.x, c.y, this.me.x, this.me.y) <= 2) {
                            if (c.unit === 0)
                                this.enemyCastles = (bc19.Logistics.findOpposite(this.r, c.x, c.y, this.symmetry));
                            var sig = c.signal;
                            this.castle[1] = c.x;
                            this.castle[0] = c.y;
                            this.extractSignal(sig);
                            break;
                        }
                    }
                }
                if (this.enemyCastles != null) {
                    for (var y = 0; y < this.blockers.length; y++) {
                        for (var x = 0; x < this.blockers[0].length; x++) {
                            for (var index167 = 0; index167 < this.enemyCastles.length; index167++) {
                                var c = this.enemyCastles[index167];
                                {
                                    if (bc19.Pathing.distance(c[1], c[0], x, y) <= 100)
                                        this.blockers[y][x] = true;
                                }
                            }
                        }
                        ;
                    }
                    ;
                }
                this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                this.endLocs[this.toGo[0]][this.toGo[1]] = true;
                this.Rmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
                this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                this.endLocs[this.castle[0]][this.castle[1]] = true;
                this.Cmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
            }
            else
                this.even = !this.even;
            if (this.enemyCastles != null) {
                for (var y = 0; y < this.blockers.length; y++) {
                    for (var x = 0; x < this.blockers[0].length; x++) {
                        for (var index168 = 0; index168 < this.enemyCastles.length; index168++) {
                            var c = this.enemyCastles[index168];
                            {
                                if (bc19.Pathing.distance(c[1], c[0], x, y) <= 100)
                                    this.blockers[y][x] = true;
                            }
                        }
                    }
                    ;
                }
                ;
            }
            if (this.me.turn % 10 === 0) {
                this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                this.endLocs[this.toGo[0]][this.toGo[1]] = true;
                this.Rmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
                this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                    return undefined;
                }
                else {
                    var array = [];
                    for (var i = 0; i < dims[0]; i++) {
                        array.push(allocate(dims.slice(1)));
                    }
                    return array;
                } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                this.endLocs[this.castle[0]][this.castle[1]] = true;
                this.Cmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
            }
            if (!this.deposit) {
                for (var index169 = 0; index169 < visible.length; index169++) {
                    var other = visible[index169];
                    {
                        if (other.x === this.castle[1] && other.y === this.castle[0] && this.r.isRadioing(other)) {
                            if (other.signal === 7777)
                                this.deposit = true;
                        }
                    }
                }
                var enemies = ([]);
                this.running = false;
                for (var index170 = 0; index170 < visible.length; index170++) {
                    var other = visible[index170];
                    {
                        if (other.team !== this.me.team && other.unit !== 1 && other.unit !== 2) {
                            if (this.me.karbonite > 0 || this.me.fuel > 0) {
                                this.deposit = true;
                                break;
                            }
                            else {
                                var dist = bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y);
                                if (dist <= 64) {
                                    this.running = true;
                                }
                                var enemyCoord = new Array(2);
                                enemyCoord[1] = other.x;
                                enemyCoord[0] = other.y;
                                /* add */ (enemies.push(enemyCoord) > 0);
                            }
                        }
                    }
                }
                if (!(enemies.length == 0)) {
                    if (this.running) {
                        var move = bc19.Pathing.retreatMove(this.r, this.me.x, this.me.y, enemies, 4, this.blockers);
                        var a = this.r.move(move[1], move[0]);
                        if (!(function (o1, o2) { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(a, null))
                            return a;
                    }
                    else {
                        return null;
                    }
                }
                if (this.me.fuel === 100 || this.me.karbonite === 20) {
                    var dRange = false;
                    var castleDist = 9999;
                    for (var index171 = 0; index171 < visible.length; index171++) {
                        var other = visible[index171];
                        {
                            if (other.team === this.me.team && (other.unit === 0 || other.unit === 1)) {
                                var newCastleDist = bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y);
                                if (newCastleDist <= 36 && newCastleDist < castleDist) {
                                    castleDist = newCastleDist;
                                    dRange = true;
                                    this.castle[0] = other.y;
                                    this.castle[1] = other.x;
                                    this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                                        return undefined;
                                    }
                                    else {
                                        var array = [];
                                        for (var i = 0; i < dims[0]; i++) {
                                            array.push(allocate(dims.slice(1)));
                                        }
                                        return array;
                                    } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                                    this.endLocs[this.castle[0]][this.castle[1]] = true;
                                    this.Cmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
                                }
                            }
                        }
                    }
                    if (!dRange && this.r.karbonite >= 50 && this.r.fuel >= 400) {
                        var range = 36;
                        var steps = (Math.floor(Math.sqrt(range)) | 0);
                        var churchTile = ([]);
                        var maxResources = 0;
                        var minDist = 9999;
                        var toBuild = new Array(2);
                        for (var dy = -1; dy <= 1; dy++) {
                            for (var dx = -1; dx <= 1; dx++) {
                                var xadj = this.me.x + dx;
                                var yadj = this.me.y + dy;
                                if (bc19.Pathing.checkBounds(this.r, xadj, yadj, this.blockers) && !this.r.getFuelMap()[yadj][xadj] && !this.r.getKarboniteMap()[yadj][xadj]) {
                                    var tile = new Array(2);
                                    tile[1] = dx;
                                    tile[0] = dy;
                                    /* add */ (churchTile.push(tile) > 0);
                                }
                            }
                            ;
                        }
                        ;
                        for (var index172 = 0; index172 < churchTile.length; index172++) {
                            var tile = churchTile[index172];
                            {
                                var numResources = 0;
                                var newDist = 0;
                                for (var y = 0 - steps; y <= steps; y++) {
                                    for (var x = 0 - steps; x <= steps; x++) {
                                        var dist = bc19.Pathing.distance(0, 0, x, y);
                                        var xCor = tile[1] + x + this.me.x;
                                        var yCor = tile[0] + y + this.me.y;
                                        if (dist <= range && bc19.Pathing.checkBounds(this.r, xCor, yCor, this.blockers) && (this.r.getFuelMap()[yCor][xCor] || this.r.getKarboniteMap()[yCor][xCor])) {
                                            numResources += 1;
                                            newDist += bc19.Pathing.distance(this.me.x + tile[1], this.me.y + tile[0], xCor, yCor);
                                        }
                                    }
                                    ;
                                }
                                ;
                                if (numResources > maxResources) {
                                    maxResources = numResources;
                                    minDist = newDist;
                                    this.castle[1] = this.me.x + tile[1];
                                    this.castle[0] = this.me.y + tile[0];
                                    toBuild[1] = tile[1];
                                    toBuild[0] = tile[0];
                                }
                                if (numResources === maxResources) {
                                    if (newDist < minDist) {
                                        minDist = newDist;
                                        this.castle[1] = this.me.x + tile[1];
                                        this.castle[0] = this.me.y + tile[0];
                                        toBuild[1] = tile[1];
                                        toBuild[0] = tile[0];
                                    }
                                }
                            }
                        }
                        this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                            return undefined;
                        }
                        else {
                            var array = [];
                            for (var i = 0; i < dims[0]; i++) {
                                array.push(allocate(dims.slice(1)));
                            }
                            return array;
                        } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                        this.endLocs[this.castle[0]][this.castle[1]] = true;
                        this.Cmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
                        var send = 1;
                        if (this.even)
                            send = 2;
                        this.r.signal(send, 2);
                        return this.r.buildUnit(1, toBuild[1], toBuild[0]);
                    }
                    this.deposit = true;
                }
            }
            if (bc19.Pathing.distance(this.me.x, this.me.y, this.castle[1], this.castle[0]) <= 2 && this.deposit) {
                this.deposit = false;
                return this.r.give(this.castle[1] - this.me.x, this.castle[0] - this.me.y, this.me.karbonite, this.me.fuel);
            }
            else if (this.me.x === this.toGo[1] && this.me.y === this.toGo[0] && !this.deposit) {
                if (this.me.karbonite === 0 && this.me.fuel === 0) {
                    var dRange = false;
                    var castleDist = 9999;
                    for (var index173 = 0; index173 < visible.length; index173++) {
                        var other = visible[index173];
                        {
                            if (other.team === this.me.team && (other.unit === 0 || other.unit === 1)) {
                                var newCastleDist = bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y);
                                if (newCastleDist <= 36 && newCastleDist < castleDist) {
                                    castleDist = newCastleDist;
                                    dRange = true;
                                    this.castle[0] = other.y;
                                    this.castle[1] = other.x;
                                    this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                                        return undefined;
                                    }
                                    else {
                                        var array = [];
                                        for (var i = 0; i < dims[0]; i++) {
                                            array.push(allocate(dims.slice(1)));
                                        }
                                        return array;
                                    } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                                    this.endLocs[this.castle[0]][this.castle[1]] = true;
                                    this.Cmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
                                }
                            }
                        }
                    }
                    if (!dRange && this.r.karbonite >= 100 && this.r.fuel >= 400) {
                        var range = 36;
                        var steps = (Math.floor(Math.sqrt(range)) | 0);
                        var churchTile = ([]);
                        var maxResources = 0;
                        var minDist = 9999;
                        var toBuild = new Array(2);
                        for (var dy = -1; dy <= 1; dy++) {
                            for (var dx = -1; dx <= 1; dx++) {
                                var xadj = this.me.x + dx;
                                var yadj = this.me.y + dy;
                                if (bc19.Pathing.checkBounds(this.r, xadj, yadj, this.blockers) && !this.r.getFuelMap()[yadj][xadj] && !this.r.getKarboniteMap()[yadj][xadj]) {
                                    var tile = new Array(2);
                                    tile[1] = dx;
                                    tile[0] = dy;
                                    /* add */ (churchTile.push(tile) > 0);
                                }
                            }
                            ;
                        }
                        ;
                        for (var index174 = 0; index174 < churchTile.length; index174++) {
                            var tile = churchTile[index174];
                            {
                                var numResources = 0;
                                var newDist = 0;
                                for (var y = 0 - steps; y <= steps; y++) {
                                    for (var x = 0 - steps; x <= steps; x++) {
                                        var dist = bc19.Pathing.distance(0, 0, x, y);
                                        var xCor = tile[1] + x + this.me.x;
                                        var yCor = tile[0] + y + this.me.y;
                                        if (dist <= range && bc19.Pathing.checkBounds(this.r, xCor, yCor, this.blockers) && (this.r.getFuelMap()[yCor][xCor] || this.r.getKarboniteMap()[yCor][xCor])) {
                                            numResources += 1;
                                            newDist += bc19.Pathing.distance(this.me.x + tile[1], this.me.y + tile[0], xCor, yCor);
                                        }
                                    }
                                    ;
                                }
                                ;
                                if (numResources > maxResources) {
                                    maxResources = numResources;
                                    minDist = newDist;
                                    this.castle[1] = this.me.x + tile[1];
                                    this.castle[0] = this.me.y + tile[0];
                                    toBuild[1] = tile[1];
                                    toBuild[0] = tile[0];
                                }
                                if (numResources === maxResources) {
                                    if (newDist < minDist) {
                                        minDist = newDist;
                                        this.castle[1] = this.me.x + tile[1];
                                        this.castle[0] = this.me.y + tile[0];
                                        toBuild[1] = tile[1];
                                        toBuild[0] = tile[0];
                                    }
                                }
                            }
                        }
                        this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                            return undefined;
                        }
                        else {
                            var array = [];
                            for (var i = 0; i < dims[0]; i++) {
                                array.push(allocate(dims.slice(1)));
                            }
                            return array;
                        } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
                        this.endLocs[this.castle[0]][this.castle[1]] = true;
                        this.Cmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
                        var send = 1;
                        if (this.even)
                            send = 2;
                        this.r.signal(send, 2);
                        return this.r.buildUnit(1, toBuild[1], toBuild[0]);
                    }
                }
                return this.r.mine();
            }
            if (this.deposit) {
                var move = this.nextMove(this.Cmap, this.castle);
                if (move == null) {
                    return null;
                }
                return this.r.move(move[1] - this.me.x, move[0] - this.me.y);
            }
            else {
                var move = this.nextMove(this.Rmap, this.toGo);
                if (move == null) {
                    return null;
                }
                return this.r.move(move[1] - this.me.x, move[0] - this.me.y);
            }
        };
        return PilgrimBot;
    }(bc19.Bot));
    bc19.PilgrimBot = PilgrimBot;
    PilgrimBot["__class"] = "bc19.PilgrimBot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var ProphetBot = (function (_super) {
        __extends(ProphetBot, _super);
        function ProphetBot(r) {
            var _this = _super.call(this, r) || this;
            _this.targets = null;
            _this.toGo = null;
            _this.running = false;
            _this.endLocs = null;
            _this.castle = null;
            _this.castleBot = null;
            _this.castleId = 0;
            _this.Rmap = null;
            _this.strat = 0;
            _this.ready = false;
            _this.church = false;
            _this.targets = ([]);
            _this.strat = 0;
            return _this;
        }
        ProphetBot.prototype.calcMap = function () {
            this.endLocs = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return undefined;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
            if (!this.ready) {
                this.endLocs[this.toGo[0]][this.toGo[1]] = true;
            }
            else {
                for (var index175 = 0; index175 < this.targets.length; index175++) {
                    var c = this.targets[index175];
                    this.endLocs[c[0]][c[1]] = true;
                }
            }
            this.Rmap = bc19.Pathing.rangeBFS(this.r, this.endLocs, 4, this.blockers, new bc19.Task());
        };
        ProphetBot.prototype.extractSig = function (signal) {
            var st = (Math.floor((signal / 10000 | 0)) | 0);
            var sig = signal % 10000;
            var ycor = sig % 100;
            var xcor = (Math.floor(((sig - ycor) / 100 | 0)) | 0);
            if (this.strat === 0) {
                if (st === 2)
                    this.even = true;
                this.strat = st;
                this.toGo[0] = ycor;
                this.toGo[1] = xcor;
                this.calcMap();
                return;
            }
            if (this.church)
                return;
            if (st === 1 || st === 3) {
                var targ = new Array(2);
                targ[1] = xcor;
                targ[0] = ycor;
                var have = false;
                for (var i = 0; i < this.targets.length; i++) {
                    if (this.targets[i][1] === xcor && this.targets[i][0] === ycor) {
                        have = true;
                        break;
                    }
                }
                ;
                if (!have) {
                    /* add */ (this.targets.push(targ) > 0);
                }
                if (st === 3) {
                    this.ready = true;
                    this.calcMap();
                }
            }
        };
        ProphetBot.prototype.extractSignal = function (signal) {
            this.strat = (Math.floor((signal / 10000 | 0)) | 0);
            if (this.strat === 0) {
                return;
            }
            else if (this.strat === 1 || this.strat === 2 || this.strat === 3) {
                var targ = new Array(2);
                var sig = signal % 10000;
                var ycor = sig % 100;
                var xcor = (Math.floor(((sig - ycor) / 100 | 0)) | 0);
                this.toGo[0] = ycor;
                this.toGo[1] = xcor;
                targ[1] = xcor;
                targ[0] = ycor;
                var have = false;
                for (var i = 0; i < this.targets.length; i++) {
                    if (this.targets[i][1] === xcor && this.targets[i][0] === ycor) {
                        have = true;
                        break;
                    }
                }
                ;
                if (!have) {
                    /* add */ (this.targets.push(targ) > 0);
                }
                if (this.strat === 2 || this.strat === 3) {
                    if (this.strat === 3)
                        this.ready = true;
                    this.calcMap();
                }
            }
        };
        ProphetBot.prototype.nextMove = function (map) {
            var min = 9999;
            var curr;
            var nullMap = (function (dims) { var allocate = function (dims) { if (dims.length == 0) {
                return 0;
            }
            else {
                var array = [];
                for (var i = 0; i < dims[0]; i++) {
                    array.push(allocate(dims.slice(1)));
                }
                return array;
            } }; return allocate(dims); })([this.r.map.length, this.r.map[0].length]);
            for (var y = 0; y < nullMap.length; y++) {
                for (var x = 0; x < nullMap.length; x++) {
                    nullMap[y][x] = 99;
                }
                ;
            }
            ;
            var moves = bc19.Pathing.findRange(this.r, this.me.x, this.me.y, 4, this.blockers, nullMap);
            for (var index176 = 0; index176 < moves.length; index176++) {
                var x = moves[index176];
                {
                    if (map[x[0]][x[1]] < min) {
                        min = map[x[0]][x[1]];
                        curr = x;
                    }
                }
            }
            return curr;
        };
        ProphetBot.prototype.act = function () {
            var visible = this.r.getVisibleRobots();
            if (this.me.turn === 1) {
                this.toGo = new Array(2);
                this.castle = new Array(2);
                for (var index177 = 0; index177 < visible.length; index177++) {
                    var c = visible[index177];
                    {
                        if (c.unit <= 1 && c.team === this.me.team && bc19.Pathing.distance(c.x, c.y, this.me.x, this.me.y) <= 2) {
                            this.castleId = c.id;
                            this.castle[1] = c.x;
                            this.castle[0] = c.y;
                            var symmetry = bc19.Logistics.symmetry(this.r.map, this.r);
                            if (c.unit === 0) {
                                var array179 = bc19.Logistics.findOpposite(this.r, this.castle[1], this.castle[0], symmetry);
                                for (var index178 = 0; index178 < array179.length; index178++) {
                                    var coor = array179[index178];
                                    /* add */ (this.targets.push(coor) > 0);
                                }
                            }
                            break;
                        }
                    }
                }
            }
            else
                this.even = !this.even;
            for (var index180 = 0; index180 < visible.length; index180++) {
                var other = visible[index180];
                {
                    if (other.x === this.castle[1] && other.y === this.castle[0] && this.r.isRadioing(other)) {
                        var sig = other.signal;
                        if ((Math.floor((sig / 10000 | 0)) | 0) !== 0)
                            this.extractSig(sig);
                    }
                }
            }
            if (this.even)
                this.r.castleTalk(this.toGo[1]);
            else
                this.r.castleTalk(this.toGo[0]);
            if (this.ready) {
                for (var i = 0; i < this.targets.length; i++) {
                    var c = this.targets[i];
                    var dist = bc19.Pathing.distance(c[1], c[0], this.me.x, this.me.y);
                    if (dist <= 8) {
                        var found = false;
                        for (var index181 = 0; index181 < visible.length; index181++) {
                            var g = visible[index181];
                            {
                                if (g.unit === 0 && g.team !== this.me.team) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            /* remove */ this.targets.splice(i, 1);
                            this.calcMap();
                            i--;
                        }
                    }
                }
                ;
            }
            var enemySeen = false;
            var minRange = false;
            var enemies = ([]);
            var preacher = false;
            for (var index182 = 0; index182 < visible.length; index182++) {
                var other = visible[index182];
                {
                    if (this.r.isVisible(other) && other.team !== this.me.team) {
                        if (other.unit === 5)
                            preacher = true;
                        enemySeen = true;
                        var dist = bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y);
                        var enemyCoord = new Array(2);
                        enemyCoord[1] = other.x;
                        enemyCoord[0] = other.y;
                        /* add */ (enemies.push(enemyCoord) > 0);
                        this.r.log("Enemy At: " + other.x + "," + other.y);
                        if (dist <= 16 || (preacher && bc19.Pathing.distance(other.x, other.y, this.me.x, this.me.y) < 25)) {
                            minRange = true;
                            this.r.log("Start Running");
                        }
                    }
                }
            }
            if (minRange || (preacher && bc19.Pathing.distance(this.castle[1], this.castle[0], this.me.x, this.me.y) <= 2)) {
                var move = bc19.Pathing.retreatMove(this.r, this.me.x, this.me.y, enemies, 4, this.blockers);
                var a = this.r.move(move[1], move[0]);
                if (!(function (o1, o2) { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(a, null))
                    return a;
            }
            if (enemySeen) {
                var atk = this.attack();
                if (!(function (o1, o2) { if (o1 && o1.equals) {
                    return o1.equals(o2);
                }
                else {
                    return o1 === o2;
                } })(atk, null))
                    return atk;
            }
            if (this.ready || !(this.me.x === this.toGo[1] && this.me.y === this.toGo[0]) || this.r.fuelMap[this.me.y][this.me.x] || this.r.karboniteMap[this.me.y][this.me.x]) {
                if (bc19.Pathing.distance(this.me.x, this.me.y, this.toGo[1], this.toGo[0]) > 4 || !this.blockers[this.toGo[0]][this.toGo[1]]) {
                    var move = this.nextMove(this.Rmap);
                    var a = this.r.move(move[1] - this.me.x, move[0] - this.me.y);
                    if (!(function (o1, o2) { if (o1 && o1.equals) {
                        return o1.equals(o2);
                    }
                    else {
                        return o1 === o2;
                    } })(a, null))
                        return a;
                }
            }
            return null;
        };
        return ProphetBot;
    }(bc19.Bot));
    bc19.ProphetBot = ProphetBot;
    ProphetBot["__class"] = "bc19.ProphetBot";
})(bc19 || (bc19 = {}));
(function (bc19) {
    var PreacherBot = (function (_super) {
        __extends(PreacherBot, _super);
        function PreacherBot(r) {
            return _super.call(this, r) || this;
        }
        PreacherBot.prototype.act = function () {
            return null;
        };
        return PreacherBot;
    }(bc19.Bot));
    bc19.PreacherBot = PreacherBot;
    PreacherBot["__class"] = "bc19.PreacherBot";
})(bc19 || (bc19 = {}));
//# sourceMappingURL=bundle.js.map
var specs = {"COMMUNICATION_BITS":16,"CASTLE_TALK_BITS":8,"MAX_ROUNDS":1000,"TRICKLE_FUEL":25,"INITIAL_KARBONITE":100,"INITIAL_FUEL":500,"MINE_FUEL_COST":1,"KARBONITE_YIELD":2,"FUEL_YIELD":10,"MAX_TRADE":1024,"MAX_BOARD_SIZE":64,"MAX_ID":4096,"CASTLE":0,"CHURCH":1,"PILGRIM":2,"CRUSADER":3,"PROPHET":4,"PREACHER":5,"RED":0,"BLUE":1,"CHESS_INITIAL":100,"CHESS_EXTRA":20,"TURN_MAX_TIME":200,"MAX_MEMORY":50000000,"UNITS":[{"CONSTRUCTION_KARBONITE":null,"CONSTRUCTION_FUEL":null,"KARBONITE_CAPACITY":null,"FUEL_CAPACITY":null,"SPEED":0,"FUEL_PER_MOVE":null,"STARTING_HP":200,"VISION_RADIUS":100,"ATTACK_DAMAGE":10,"ATTACK_RADIUS":[1,64],"ATTACK_FUEL_COST":10,"DAMAGE_SPREAD":0},{"CONSTRUCTION_KARBONITE":50,"CONSTRUCTION_FUEL":200,"KARBONITE_CAPACITY":null,"FUEL_CAPACITY":null,"SPEED":0,"FUEL_PER_MOVE":null,"STARTING_HP":100,"VISION_RADIUS":100,"ATTACK_DAMAGE":0,"ATTACK_RADIUS":0,"ATTACK_FUEL_COST":0,"DAMAGE_SPREAD":0},{"CONSTRUCTION_KARBONITE":10,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":4,"FUEL_PER_MOVE":1,"STARTING_HP":10,"VISION_RADIUS":100,"ATTACK_DAMAGE":null,"ATTACK_RADIUS":null,"ATTACK_FUEL_COST":null,"DAMAGE_SPREAD":null},{"CONSTRUCTION_KARBONITE":15,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":9,"FUEL_PER_MOVE":1,"STARTING_HP":40,"VISION_RADIUS":49,"ATTACK_DAMAGE":10,"ATTACK_RADIUS":[1,16],"ATTACK_FUEL_COST":10,"DAMAGE_SPREAD":0},{"CONSTRUCTION_KARBONITE":25,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":4,"FUEL_PER_MOVE":2,"STARTING_HP":20,"VISION_RADIUS":64,"ATTACK_DAMAGE":10,"ATTACK_RADIUS":[16,64],"ATTACK_FUEL_COST":25,"DAMAGE_SPREAD":0},{"CONSTRUCTION_KARBONITE":30,"CONSTRUCTION_FUEL":50,"KARBONITE_CAPACITY":20,"FUEL_CAPACITY":100,"SPEED":4,"FUEL_PER_MOVE":3,"STARTING_HP":60,"VISION_RADIUS":16,"ATTACK_DAMAGE":20,"ATTACK_RADIUS":[1,16],"ATTACK_FUEL_COST":15,"DAMAGE_SPREAD":3}]};
var robot = new bc19.MyRobot(); robot.setSpecs(specs);